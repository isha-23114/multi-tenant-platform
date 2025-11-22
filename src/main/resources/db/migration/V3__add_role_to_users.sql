ALTER TABLE users
ADD COLUMN role VARCHAR(50) DEFAULT 'TENANT_USER';

-- add composite unique if not already present (tenant+email)
CREATE UNIQUE INDEX IF NOT EXISTS ux_users_tenant_email ON users (tenant_key, lower(email));