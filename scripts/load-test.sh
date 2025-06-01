#!/bin/bash

# MediSys Load Testing Script
# This script performs comprehensive load testing using Apache Bench (ab) and curl

set -e

# Configuration
BASE_URL="http://localhost:8080"
HEALTH_URL="http://localhost:8081/actuator/health"
METRICS_URL="http://localhost:8081/actuator/metrics"
CONCURRENT_USERS=50
TOTAL_REQUESTS=5000
TEST_DURATION=300  # 5 minutes

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging function
log() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log "Checking prerequisites..."
    
    if ! command -v ab &> /dev/null; then
        error "Apache Bench (ab) is not installed. Please install apache2-utils"
        exit 1
    fi
    
    if ! command -v curl &> /dev/null; then
        error "curl is not installed"
        exit 1
    fi
    
    if ! command -v jq &> /dev/null; then
        warning "jq is not installed. JSON parsing will be limited"
    fi
    
    success "Prerequisites check completed"
}

# Check application health
check_health() {
    log "Checking application health..."
    
    local health_response=$(curl -s -w "%{http_code}" -o /tmp/health_response.json "$HEALTH_URL" || echo "000")
    
    if [ "$health_response" = "200" ]; then
        success "Application is healthy"
        if command -v jq &> /dev/null; then
            cat /tmp/health_response.json | jq '.'
        else
            cat /tmp/health_response.json
        fi
    else
        error "Application health check failed (HTTP $health_response)"
        exit 1
    fi
}

# Baseline metrics collection
collect_baseline_metrics() {
    log "Collecting baseline metrics..."
    
    mkdir -p test-results/$(date +%Y%m%d_%H%M%S)
    local result_dir="test-results/$(date +%Y%m%d_%H%M%S)"
    
    # System metrics
    echo "=== System Metrics Baseline ===" > "$result_dir/baseline_metrics.txt"
    echo "Date: $(date)" >> "$result_dir/baseline_metrics.txt"
    echo "Memory Usage:" >> "$result_dir/baseline_metrics.txt"
    free -h >> "$result_dir/baseline_metrics.txt"
    echo "" >> "$result_dir/baseline_metrics.txt"
    
    echo "CPU Usage:" >> "$result_dir/baseline_metrics.txt"
    top -bn1 | grep "Cpu(s)" >> "$result_dir/baseline_metrics.txt"
    echo "" >> "$result_dir/baseline_metrics.txt"
    
    echo "Disk Usage:" >> "$result_dir/baseline_metrics.txt"
    df -h >> "$result_dir/baseline_metrics.txt"
    echo "" >> "$result_dir/baseline_metrics.txt"
    
    # Application metrics
    curl -s "$METRICS_URL" > "$result_dir/baseline_app_metrics.json"
    
    echo "$result_dir"
}

# Health endpoint load test
test_health_endpoint() {
    local result_dir=$1
    log "Testing health endpoint performance..."
    
    ab -n $TOTAL_REQUESTS -c $CONCURRENT_USERS "$HEALTH_URL" > "$result_dir/health_endpoint_test.txt" 2>&1
    
    if [ $? -eq 0 ]; then
        success "Health endpoint test completed"
        grep -E "(Requests per second|Time per request|Transfer rate)" "$result_dir/health_endpoint_test.txt"
    else
        error "Health endpoint test failed"
    fi
}

# Metrics endpoint load test
test_metrics_endpoint() {
    local result_dir=$1
    log "Testing metrics endpoint performance..."
    
    ab -n 1000 -c 10 "$METRICS_URL" > "$result_dir/metrics_endpoint_test.txt" 2>&1
    
    if [ $? -eq 0 ]; then
        success "Metrics endpoint test completed"
        grep -E "(Requests per second|Time per request|Transfer rate)" "$result_dir/metrics_endpoint_test.txt"
    else
        error "Metrics endpoint test failed"
    fi
}

# Sustained load test
sustained_load_test() {
    local result_dir=$1
    log "Running sustained load test for $TEST_DURATION seconds..."
    
    # Start background monitoring
    monitor_resources "$result_dir" &
    local monitor_pid=$!
    
    # Run sustained load
    timeout $TEST_DURATION ab -n 999999 -c $CONCURRENT_USERS -t $TEST_DURATION "$HEALTH_URL" > "$result_dir/sustained_load_test.txt" 2>&1
    
    # Stop monitoring
    kill $monitor_pid 2>/dev/null || true
    
    success "Sustained load test completed"
    grep -E "(Requests per second|Time per request|Failed requests)" "$result_dir/sustained_load_test.txt"
}

# Resource monitoring during tests
monitor_resources() {
    local result_dir=$1
    local monitor_file="$result_dir/resource_monitoring.txt"
    
    echo "=== Resource Monitoring During Load Test ===" > "$monitor_file"
    echo "Timestamp,CPU%,Memory%,DiskIO" >> "$monitor_file"
    
    while true; do
        local timestamp=$(date '+%Y-%m-%d %H:%M:%S')
        local cpu_usage=$(top -bn1 | grep "Cpu(s)" | awk '{print $2}' | cut -d'%' -f1)
        local memory_usage=$(free | grep Mem | awk '{printf "%.1f", $3/$2 * 100.0}')
        local disk_io=$(iostat -d 1 1 2>/dev/null | tail -n +4 | awk '{sum+=$4} END {print sum}' || echo "N/A")
        
        echo "$timestamp,$cpu_usage,$memory_usage,$disk_io" >> "$monitor_file"
        sleep 5
    done
}

# Memory leak detection
detect_memory_leaks() {
    local result_dir=$1
    log "Running memory leak detection..."
    
    local initial_memory=$(curl -s "$METRICS_URL/jvm.memory.used" | jq -r '.measurements[0].value' 2>/dev/null || echo "0")
    
    # Run intensive operations
    for i in {1..10}; do
        ab -n 1000 -c 20 "$HEALTH_URL" > /dev/null 2>&1
        sleep 10
        
        local current_memory=$(curl -s "$METRICS_URL/jvm.memory.used" | jq -r '.measurements[0].value' 2>/dev/null || echo "0")
        echo "Iteration $i: Memory usage: $current_memory bytes" >> "$result_dir/memory_leak_test.txt"
    done
    
    local final_memory=$(curl -s "$METRICS_URL/jvm.memory.used" | jq -r '.measurements[0].value' 2>/dev/null || echo "0")
    
    echo "Initial Memory: $initial_memory bytes" >> "$result_dir/memory_leak_test.txt"
    echo "Final Memory: $final_memory bytes" >> "$result_dir/memory_leak_test.txt"
    
    if [ "$final_memory" -gt "$((initial_memory * 2))" ]; then
        warning "Potential memory leak detected - memory usage doubled"
    else
        success "No significant memory leaks detected"
    fi
}

# Database connection pool test
test_connection_pool() {
    local result_dir=$1
    log "Testing database connection pool under load..."
    
    # Simulate high database load
    for i in {1..5}; do
        ab -n 500 -c 50 "$HEALTH_URL" > /dev/null 2>&1 &
    done
    
    wait
    
    # Check for connection pool metrics
    curl -s "$METRICS_URL" | grep -i "hikari\|connection" > "$result_dir/connection_pool_test.txt" 2>/dev/null || echo "Connection pool metrics not available" > "$result_dir/connection_pool_test.txt"
    
    success "Connection pool test completed"
}

# Generate performance report
generate_report() {
    local result_dir=$1
    log "Generating performance report..."
    
    local report_file="$result_dir/performance_report.html"
    
    cat > "$report_file" << EOF
<!DOCTYPE html>
<html>
<head>
    <title>MediSys Performance Test Report</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { background-color: #f0f0f0; padding: 20px; border-radius: 5px; }
        .section { margin: 20px 0; padding: 15px; border: 1px solid #ddd; border-radius: 5px; }
        .success { color: green; }
        .warning { color: orange; }
        .error { color: red; }
        pre { background-color: #f5f5f5; padding: 10px; overflow-x: auto; }
    </style>
</head>
<body>
    <div class="header">
        <h1>MediSys Performance Test Report</h1>
        <p>Generated on: $(date)</p>
        <p>Test Configuration: $CONCURRENT_USERS concurrent users, $TOTAL_REQUESTS total requests</p>
    </div>
    
    <div class="section">
        <h2>Test Summary</h2>
        <ul>
            <li>Health Endpoint Test: $([ -f "$result_dir/health_endpoint_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
            <li>Metrics Endpoint Test: $([ -f "$result_dir/metrics_endpoint_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
            <li>Sustained Load Test: $([ -f "$result_dir/sustained_load_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
            <li>Memory Leak Detection: $([ -f "$result_dir/memory_leak_test.txt" ] && echo "✅ Completed" || echo "❌ Failed")</li>
        </ul>
    </div>
    
    <div class="section">
        <h2>Performance Metrics</h2>
        <h3>Health Endpoint Performance</h3>
        <pre>$(grep -E "(Requests per second|Time per request|Failed requests)" "$result_dir/health_endpoint_test.txt" 2>/dev/null || echo "Data not available")</pre>
        
        <h3>Sustained Load Test Results</h3>
        <pre>$(grep -E "(Requests per second|Time per request|Failed requests)" "$result_dir/sustained_load_test.txt" 2>/dev/null || echo "Data not available")</pre>
    </div>
    
    <div class="section">
        <h2>System Resources</h2>
        <h3>Baseline Metrics</h3>
        <pre>$(cat "$result_dir/baseline_metrics.txt" 2>/dev/null || echo "Data not available")</pre>
    </div>
    
    <div class="section">
        <h2>Recommendations</h2>
        <ul>
            <li>Monitor response times under sustained load</li>
            <li>Check for memory leaks during extended operations</li>
            <li>Verify database connection pool efficiency</li>
            <li>Consider horizontal scaling if throughput requirements increase</li>
        </ul>
    </div>
</body>
</html>
EOF

    success "Performance report generated: $report_file"
}

# Main execution
main() {
    log "Starting MediSys Load Testing Suite"
    
    check_prerequisites
    check_health
    
    local result_dir=$(collect_baseline_metrics)
    
    test_health_endpoint "$result_dir"
    test_metrics_endpoint "$result_dir"
    sustained_load_test "$result_dir"
    detect_memory_leaks "$result_dir"
    test_connection_pool "$result_dir"
    
    generate_report "$result_dir"
    
    success "Load testing completed. Results saved in: $result_dir"
}

# Run main function
main "$@"
