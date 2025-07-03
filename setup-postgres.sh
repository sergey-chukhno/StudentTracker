#!/bin/bash
# setup-postgres.sh
# Usage: bash setup-postgres.sh

DB_NAME=student_tracker
DB_USER=postgres
DB_PASS=postgres

# Create database user (if not exists)
sudo -u postgres psql -c "DO $$ BEGIN IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '$DB_USER') THEN CREATE ROLE $DB_USER LOGIN PASSWORD '$DB_PASS'; END IF; END $$;"

# Create database (if not exists)
sudo -u postgres psql -c "CREATE DATABASE $DB_NAME OWNER $DB_USER;"

# Grant all privileges
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;" 