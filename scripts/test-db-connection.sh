#!/bin/bash

# Test database connection script for MediSys

echo "Testing PostgreSQL connection..."

# Test if PostgreSQL is running
if ! pgrep -x "postgres" > /dev/null; then
    echo "❌ PostgreSQL is not running"
    exit 1
fi

echo "✅ PostgreSQL is running"

# Test database connection
if psql -h localhost -U postgres -d postgres -c "SELECT 1;" > /dev/null 2>&1; then
    echo "✅ Can connect to PostgreSQL"
else
    echo "❌ Cannot connect to PostgreSQL"
    exit 1
fi

# Check if medisys database exists
if psql -h localhost -U postgres -lqt | cut -d \| -f 1 | grep -qw medisys; then
    echo "✅ MediSys database exists"
else
    echo "⚠️  MediSys database does not exist. Creating..."
    
    # Create database and user
    psql -h localhost -U postgres -c "CREATE DATABASE medisys;" 2>/dev/null || echo "Database might already exist"
    psql -h localhost -U postgres -c "CREATE USER medisys_user WITH PASSWORD 'medisys_password';" 2>/dev/null || echo "User might already exist"
    psql -h localhost -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE medisys TO medisys_user;" 2>/dev/null
    
    echo "✅ Database and user created"
fi

# Test connection with medisys_user
if PGPASSWORD=medisys_password psql -h localhost -U medisys_user -d medisys -c "SELECT 1;" > /dev/null 2>&1; then
    echo "✅ Can connect as medisys_user"
else
    echo "❌ Cannot connect as medisys_user"
    echo "Trying to fix permissions..."
    
    psql -h localhost -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE medisys TO medisys_user;"
    psql -h localhost -U postgres -d medisys -c "GRANT ALL PRIVILEGES ON SCHEMA public TO medisys_user;"
    
    if PGPASSWORD=medisys_password psql -h localhost -U medisys_user -d medisys -c "SELECT 1;" > /dev/null 2>&1; then
        echo "✅ Fixed - Can now connect as medisys_user"
    else
        echo "❌ Still cannot connect as medisys_user"
        exit 1
    fi
fi

# Setup database schema if needed
echo "Setting up database schema..."
PGPASSWORD=medisys_password psql -h localhost -U medisys_user -d medisys -f scripts/setup-test-db.sql > /dev/null 2>&1

if [ $? -eq 0 ]; then
    echo "✅ Database schema setup completed"
else
    echo "⚠️  Database schema setup had some issues (might be normal if tables already exist)"
fi

# Test if tables exist
table_count=$(PGPASSWORD=medisys_password psql -h localhost -U medisys_user -d medisys -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" 2>/dev/null | tr -d ' ')

if [ "$table_count" -gt 0 ]; then
    echo "✅ Database has $table_count tables"
else
    echo "❌ No tables found in database"
    exit 1
fi

# Test if admin user exists
admin_exists=$(PGPASSWORD=medisys_password psql -h localhost -U medisys_user -d medisys -t -c "SELECT COUNT(*) FROM users WHERE username = 'admin';" 2>/dev/null | tr -d ' ')

if [ "$admin_exists" -gt 0 ]; then
    echo "✅ Admin user exists in database"
else
    echo "❌ Admin user not found in database"
    exit 1
fi

echo ""
echo "🎉 Database connection test completed successfully!"
echo ""
echo "You can now login with:"
echo "Username: admin"
echo "Password: admin123"
echo ""
echo "Other test users:"
echo "- doctor / admin123"
echo "- finance / admin123"
echo "- dept_head / admin123"
