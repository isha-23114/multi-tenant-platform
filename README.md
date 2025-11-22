# multi-tenant-platform

This project is a multi-tenant SaaS backend platform built with Spring Boot 3, designed to support multiple isolated business clients (tenants) within a single application instance.

Each tenant gets:

* Isolated data (row-level multi-tenancy)
* Its own users & permissions
* Its own resources (Projects)
* Audit logs for all changes
* Role-based access control
* JWT authentication
* Admin-level access for tenant management

Features:

* Multi-tenancy architecture
  * Request-scoped tenant resolution via X-Tenant-ID header
  * TenantContext storing tenant ID per request
  * Tenant-aware JPA entities (tenant_key column)
  * TenantEntityListener auto-populating tenant key
  * Repository filtering via findByTenantKey(...)
  * Isolated data across tenants (ACME users cannot access BRIGHT users)

* Authentication and Authorization
  * JWT authentication
  * Password hashing
  * Role based access control (RBAC) - *TENANT_ADMIN, SUPER_ADMIN, TENANT_USER*
  * Protected endpoints — tenant users can access only their own tenant’s data

* User Management
  * Create users inside a tenant
  * List users only for the current tenant
  * Password hashing before storage

* Tenant-aware resources (Projects)
  * CRUD endpoints for Projects
  * Auto-attach tenant on CREATE
  * tenant_key applied to all queries
  * Only users from same tenant can view/modify projects

* Audit logging
  * Every CREATE / UPDATE / DELETE on Project entity is logged
  * Stored in audit_log table

* Database migrations - Flyway
  * Versioned migrations in /resources/db/migration/
  * Ensures version-controlled schema evolution

* Dockerized PostgreSQL
  * Run `docker run --name postgres-db -e POSTGRES_PASSWORD=root -e POSTGRES_DB=saas_platform -p 5432:5432 -d postgres:14` in terminal to download the image.
  * Run `docker start postgres-db` to start PostgreSQL.

Start the application from your IDE and test these APIs in Postman:

* Create tenant:
  `POST` `http://localhost:8082/api/tenants`
   
   Body: 
    ```json
  {
     "tenantKey": "craus",
     "name": "Craus Tech",
     "plan": "premium"
  }
  ```
  Response:
   ```json
  {
    "id": 3,
    "tenantKey": "craus",
    "name": "Craus Tech",
    "plan": "premium",
    "active": true
  }