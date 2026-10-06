@echo off
set PGPASSWORD=chocolatefrog
psql -h localhost -p 5433 -U student -d hogwarts -c "SELECT * FROM databasechangelog ORDER BY dateexecuted;"
psql -h localhost -p 5433 -U student -d hogwarts -c "\di"
