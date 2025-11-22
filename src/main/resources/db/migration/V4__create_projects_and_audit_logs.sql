--Projects table (tenant-scoped)
CREATE TABLE projects (
    id SERIAL PRIMARY KEY ,
    tenant_key VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_projects ON projects(tenant_key);

--Audit logs (records create, update and delete across tenant--scoped entities)
CREATE TABLE audit_logs (
    id SERIAL PRIMARY KEY,
    tenant_key VARCHAR(100) NOT NULL,
    entity_name VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
    action VARCHAR(20) NOT NULL, -- CREATE / UPDATE / DELETE
    changed_by_email VARCHAR(255),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);

CREATE INDEX idx_audit_tenant ON audit_logs(tenant_key);
CREATE INDEX idx_audit_entity ON audit_logs(entity_name, entity_id);