# MediSys Operations Manual

## Table of Contents
1. [System Architecture](#system-architecture)
2. [Deployment Procedures](#deployment-procedures)
3. [Monitoring and Alerting](#monitoring-and-alerting)
4. [Backup and Recovery](#backup-and-recovery)
5. [Security Operations](#security-operations)
6. [Performance Management](#performance-management)
7. [Troubleshooting Guide](#troubleshooting-guide)
8. [Maintenance Procedures](#maintenance-procedures)
9. [Incident Response](#incident-response)
10. [Change Management](#change-management)

## System Architecture

### Infrastructure Overview
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Load Balancer │    │   Application   │    │   Database      │
│   (HAProxy)     │◄──►│   Servers       │◄──►│   (PostgreSQL)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │              ┌─────────────────┐              │
         │              │  Redis Cache    │              │
         └──────────────┤   Cluster       ├──────────────┘
                        └─────────────────┘
                                 │
                        ┌─────────────────┐
                        │  Monitoring     │
                        │  (Prometheus)   │
                        └─────────────────┘
```

### Component Details
- **Application Servers**: 2-4 instances running JavaFX/Spring Boot
- **Database**: PostgreSQL 16.9 with streaming replication
- **Cache**: Redis 7.0 cluster with 3 nodes
- **Load Balancer**: HAProxy for high availability
- **Monitoring**: Prometheus + Grafana stack

### Network Configuration
- **Production Network**: 10.0.1.0/24
- **Database Network**: 10.0.2.0/24
- **Management Network**: 10.0.3.0/24
- **DMZ**: 192.168.1.0/24

## Deployment Procedures

### Pre-Deployment Checklist
- [ ] Code review completed and approved
- [ ] All tests passing (unit, integration, security)
- [ ] Database migration scripts tested
- [ ] Backup completed
- [ ] Rollback plan prepared
- [ ] Stakeholders notified
- [ ] Maintenance window scheduled

### Production Deployment Steps

#### 1. Preparation Phase
```bash
# 1. Create deployment directory
mkdir -p /opt/medisys/releases/$(date +%Y%m%d_%H%M%S)
cd /opt/medisys/releases/$(date +%Y%m%d_%H%M%S)

# 2. Download and verify release package
wget https://releases.medisys.com/medisys-v2.0.0.tar.gz
sha256sum -c medisys-v2.0.0.tar.gz.sha256

# 3. Extract and prepare
tar -xzf medisys-v2.0.0.tar.gz
```

#### 2. Database Migration
```bash
# 1. Backup current database
pg_dump -h db-server -U medisys_user medisys > backup_$(date +%Y%m%d_%H%M%S).sql

# 2. Run migration scripts
psql -h db-server -U medisys_user -d medisys -f migrations/v2.0.0.sql

# 3. Verify migration
psql -h db-server -U medisys_user -d medisys -c "SELECT version FROM schema_version;"
```

#### 3. Application Deployment
```bash
# 1. Stop application services
systemctl stop medisys-app-1
systemctl stop medisys-app-2

# 2. Update application files
cp -r /opt/medisys/releases/$(date +%Y%m%d_%H%M%S)/* /opt/medisys/current/

# 3. Update configuration
cp /opt/medisys/config/production.properties /opt/medisys/current/config/

# 4. Start services
systemctl start medisys-app-1
systemctl start medisys-app-2

# 5. Verify deployment
curl -f http://localhost:8081/actuator/health
```

#### 4. Post-Deployment Verification
```bash
# 1. Health checks
./scripts/health-check.sh

# 2. Smoke tests
./scripts/smoke-tests.sh

# 3. Performance verification
./scripts/performance-check.sh

# 4. Security scan
./scripts/security-scan.sh
```

### Rollback Procedures
```bash
# 1. Stop current services
systemctl stop medisys-app-*

# 2. Restore previous version
ln -sfn /opt/medisys/releases/previous /opt/medisys/current

# 3. Restore database (if needed)
psql -h db-server -U medisys_user -d medisys < backup_previous.sql

# 4. Start services
systemctl start medisys-app-*

# 5. Verify rollback
curl -f http://localhost:8081/actuator/health
```

## Monitoring and Alerting

### Key Metrics to Monitor

#### Application Metrics
- **Response Time**: 95th percentile < 500ms
- **Throughput**: > 100 requests/minute
- **Error Rate**: < 1%
- **Memory Usage**: < 80% of allocated
- **CPU Usage**: < 70% average

#### Database Metrics
- **Connection Pool**: < 80% utilization
- **Query Performance**: Slow queries > 1 second
- **Replication Lag**: < 5 seconds
- **Disk Usage**: < 85% full
- **Lock Waits**: < 100ms average

#### Infrastructure Metrics
- **Disk I/O**: < 80% utilization
- **Network**: < 70% bandwidth utilization
- **Memory**: < 85% utilization
- **Load Average**: < number of CPU cores

### Alerting Rules

#### Critical Alerts (Immediate Response)
```yaml
# Application Down
- alert: ApplicationDown
  expr: up{job="medisys-app"} == 0
  for: 1m
  labels:
    severity: critical
  annotations:
    summary: "MediSys application is down"

# Database Connection Failed
- alert: DatabaseConnectionFailed
  expr: database_connections_failed_total > 10
  for: 2m
  labels:
    severity: critical
  annotations:
    summary: "Database connection failures detected"

# High Error Rate
- alert: HighErrorRate
  expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.1
  for: 5m
  labels:
    severity: critical
  annotations:
    summary: "High error rate detected"
```

#### Warning Alerts (Monitor Closely)
```yaml
# High Memory Usage
- alert: HighMemoryUsage
  expr: (node_memory_MemTotal_bytes - node_memory_MemAvailable_bytes) / node_memory_MemTotal_bytes > 0.8
  for: 10m
  labels:
    severity: warning
  annotations:
    summary: "High memory usage detected"

# Slow Response Time
- alert: SlowResponseTime
  expr: histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m])) > 1
  for: 10m
  labels:
    severity: warning
  annotations:
    summary: "Slow response times detected"
```

### Monitoring Dashboard Setup
```bash
# 1. Install Grafana dashboards
curl -X POST \
  http://grafana:3000/api/dashboards/db \
  -H 'Content-Type: application/json' \
  -d @dashboards/medisys-overview.json

# 2. Configure data sources
curl -X POST \
  http://grafana:3000/api/datasources \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Prometheus",
    "type": "prometheus",
    "url": "http://prometheus:9090",
    "access": "proxy"
  }'
```

## Backup and Recovery

### Backup Strategy

#### Database Backups
```bash
# Daily full backup
#!/bin/bash
BACKUP_DIR="/opt/backups/database"
DATE=$(date +%Y%m%d_%H%M%S)

# Create backup
pg_dump -h db-server -U medisys_user -Fc medisys > $BACKUP_DIR/medisys_$DATE.dump

# Compress and encrypt
gpg --cipher-algo AES256 --compress-algo 1 --symmetric --output $BACKUP_DIR/medisys_$DATE.dump.gpg $BACKUP_DIR/medisys_$DATE.dump

# Remove unencrypted backup
rm $BACKUP_DIR/medisys_$DATE.dump

# Cleanup old backups (keep 30 days)
find $BACKUP_DIR -name "*.dump.gpg" -mtime +30 -delete
```

#### Application Backups
```bash
# Configuration and logs backup
#!/bin/bash
BACKUP_DIR="/opt/backups/application"
DATE=$(date +%Y%m%d_%H%M%S)

# Backup configuration
tar -czf $BACKUP_DIR/config_$DATE.tar.gz /opt/medisys/config/

# Backup logs
tar -czf $BACKUP_DIR/logs_$DATE.tar.gz /opt/medisys/logs/

# Backup uploaded files
tar -czf $BACKUP_DIR/uploads_$DATE.tar.gz /opt/medisys/uploads/
```

### Recovery Procedures

#### Database Recovery
```bash
# 1. Stop application services
systemctl stop medisys-app-*

# 2. Create recovery database
createdb -h db-server -U postgres medisys_recovery

# 3. Restore from backup
gpg --decrypt /opt/backups/database/medisys_20231201_120000.dump.gpg | pg_restore -h db-server -U medisys_user -d medisys_recovery

# 4. Verify data integrity
psql -h db-server -U medisys_user -d medisys_recovery -c "SELECT COUNT(*) FROM patients;"

# 5. Switch to recovery database
# Update configuration to point to recovery database
# Restart services
```

#### Point-in-Time Recovery
```bash
# 1. Stop PostgreSQL
systemctl stop postgresql

# 2. Restore base backup
tar -xzf /opt/backups/base_backup_20231201.tar.gz -C /var/lib/postgresql/data/

# 3. Configure recovery
cat > /var/lib/postgresql/data/recovery.conf << EOF
restore_command = 'cp /opt/backups/wal_archive/%f %p'
recovery_target_time = '2023-12-01 14:30:00'
EOF

# 4. Start PostgreSQL
systemctl start postgresql
```

## Security Operations

### Security Monitoring

#### Log Analysis
```bash
# Monitor authentication failures
tail -f /var/log/medisys/security.log | grep "AUTHENTICATION_FAILED"

# Check for suspicious activities
grep -E "(SQL_INJECTION|XSS_ATTEMPT|BRUTE_FORCE)" /var/log/medisys/security.log

# Monitor privilege escalations
grep "PRIVILEGE_ESCALATION" /var/log/medisys/audit.log
```

#### Security Scans
```bash
# Daily vulnerability scan
#!/bin/bash
nmap -sV --script vuln localhost > /var/log/security/vuln_scan_$(date +%Y%m%d).log

# SSL/TLS configuration check
testssl.sh --quiet --jsonfile /var/log/security/ssl_check_$(date +%Y%m%d).json https://medisys.example.com

# Application security scan
./scripts/security-audit.sh > /var/log/security/app_scan_$(date +%Y%m%d).log
```

### Incident Response Procedures

#### Security Incident Response
1. **Detection and Analysis**
   - Monitor security alerts
   - Analyze log files
   - Assess impact and scope

2. **Containment**
   - Isolate affected systems
   - Block malicious IP addresses
   - Disable compromised accounts

3. **Eradication**
   - Remove malware/threats
   - Patch vulnerabilities
   - Update security configurations

4. **Recovery**
   - Restore systems from clean backups
   - Verify system integrity
   - Monitor for recurring issues

5. **Post-Incident Activities**
   - Document lessons learned
   - Update security procedures
   - Conduct security training

### Access Control Management
```bash
# Review user access quarterly
#!/bin/bash
echo "=== User Access Review ===" > /tmp/access_review.txt
psql -h db-server -U medisys_user -d medisys -c "
SELECT username, role, last_login, created_date 
FROM users 
WHERE active = true 
ORDER BY last_login DESC;" >> /tmp/access_review.txt

# Check for inactive accounts
psql -h db-server -U medisys_user -d medisys -c "
SELECT username, last_login 
FROM users 
WHERE last_login < NOW() - INTERVAL '90 days' 
AND active = true;" >> /tmp/access_review.txt
```

## Performance Management

### Performance Monitoring
```bash
# Application performance check
#!/bin/bash
echo "=== Performance Report ===" > /tmp/performance_report.txt

# Check response times
curl -w "@curl-format.txt" -o /dev/null -s http://localhost:8080/api/health >> /tmp/performance_report.txt

# Check database performance
psql -h db-server -U medisys_user -d medisys -c "
SELECT query, calls, total_time, mean_time 
FROM pg_stat_statements 
ORDER BY total_time DESC 
LIMIT 10;" >> /tmp/performance_report.txt

# Check system resources
top -bn1 | head -20 >> /tmp/performance_report.txt
```

### Performance Tuning

#### Database Optimization
```sql
-- Analyze query performance
EXPLAIN (ANALYZE, BUFFERS) SELECT * FROM patients WHERE name LIKE '%John%';

-- Update table statistics
ANALYZE patients;

-- Reindex if needed
REINDEX INDEX idx_patients_name;

-- Check for missing indexes
SELECT schemaname, tablename, attname, n_distinct, correlation 
FROM pg_stats 
WHERE schemaname = 'public' 
AND n_distinct > 100;
```

#### Application Tuning
```bash
# JVM tuning for production
export JAVA_OPTS="-Xms2g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+HeapDumpOnOutOfMemoryError"

# Connection pool tuning
# In application.properties:
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

## Troubleshooting Guide

### Common Issues and Solutions

#### Application Won't Start
```bash
# Check Java version
java -version

# Check available memory
free -h

# Check port availability
netstat -tulpn | grep :8080

# Check logs
tail -f /opt/medisys/logs/application.log

# Check configuration
grep -v "^#" /opt/medisys/config/application.properties
```

#### Database Connection Issues
```bash
# Test database connectivity
psql -h db-server -U medisys_user -d medisys -c "SELECT 1;"

# Check connection pool
curl http://localhost:8081/actuator/metrics/hikaricp.connections.active

# Check database locks
psql -h db-server -U medisys_user -d medisys -c "
SELECT pid, usename, query, state 
FROM pg_stat_activity 
WHERE state = 'active';"
```

#### Performance Issues
```bash
# Check system resources
htop

# Check disk I/O
iostat -x 1

# Check network
iftop

# Check application metrics
curl http://localhost:8081/actuator/metrics | jq '.names[]' | grep -E "(response|memory|cpu)"
```

### Emergency Procedures

#### System Recovery
```bash
# Emergency restart procedure
#!/bin/bash
echo "Starting emergency recovery..."

# Stop all services
systemctl stop medisys-app-*
systemctl stop redis
systemctl stop postgresql

# Check disk space
df -h

# Clear temporary files
rm -rf /tmp/medisys-*
rm -rf /opt/medisys/logs/*.log.old

# Start services in order
systemctl start postgresql
sleep 30
systemctl start redis
sleep 10
systemctl start medisys-app-1
sleep 30
systemctl start medisys-app-2

# Verify services
./scripts/health-check.sh
```

## Maintenance Procedures

### Regular Maintenance Tasks

#### Daily Tasks
- [ ] Check system health dashboard
- [ ] Review error logs
- [ ] Verify backup completion
- [ ] Monitor disk space usage
- [ ] Check security alerts

#### Weekly Tasks
- [ ] Review performance metrics
- [ ] Update system packages
- [ ] Clean up old log files
- [ ] Test backup restoration
- [ ] Review user access logs

#### Monthly Tasks
- [ ] Security vulnerability scan
- [ ] Performance optimization review
- [ ] Database maintenance (VACUUM, ANALYZE)
- [ ] Update documentation
- [ ] Disaster recovery test

#### Quarterly Tasks
- [ ] Full security audit
- [ ] Capacity planning review
- [ ] User access review
- [ ] Update disaster recovery plan
- [ ] Staff training updates

### Maintenance Scripts
```bash
# Daily maintenance script
#!/bin/bash
LOG_FILE="/var/log/maintenance/daily_$(date +%Y%m%d).log"

echo "Starting daily maintenance - $(date)" >> $LOG_FILE

# Clean old logs
find /opt/medisys/logs -name "*.log" -mtime +7 -delete >> $LOG_FILE 2>&1

# Database maintenance
psql -h db-server -U medisys_user -d medisys -c "VACUUM ANALYZE;" >> $LOG_FILE 2>&1

# Check disk space
df -h >> $LOG_FILE

# Health check
curl -f http://localhost:8081/actuator/health >> $LOG_FILE 2>&1

echo "Daily maintenance completed - $(date)" >> $LOG_FILE
```

---

**Document Version**: 2.0  
**Last Updated**: December 2024  
**Next Review**: March 2025  
**Owner**: DevOps Team  
**Approved By**: CTO

For technical support: ops-support@medisys.com
