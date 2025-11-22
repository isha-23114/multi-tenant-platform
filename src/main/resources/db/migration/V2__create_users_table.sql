CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       tenant_key VARCHAR(100) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       full_name VARCHAR(255),
                       password_hash VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_tenant ON users (tenant_key);

-- Enforce email uniqueness per tenant
CREATE UNIQUE INDEX ux_users_tenant_email ON users (tenant_key, email);