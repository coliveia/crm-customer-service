-- TMF629 Customer Management - Database Schema
-- Oracle Autonomous Database 26ai
-- Created: 2026-02-02

-- ============================================================================
-- PARTY TABLE (Base entity)
-- ============================================================================
CREATE TABLE party (
    party_id VARCHAR2(50) PRIMARY KEY,
    party_type VARCHAR2(50) NOT NULL,
    name VARCHAR2(255) NOT NULL,
    status VARCHAR2(50) NOT NULL DEFAULT 'ACTIVE',
    valid_from TIMESTAMP,
    valid_to TIMESTAMP,
    href VARCHAR2(500),
    created_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    created_by VARCHAR2(100),
    updated_by VARCHAR2(100),
    context_data JSON,
    CONSTRAINT chk_party_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED'))
);

CREATE INDEX idx_party_type ON party(party_type);
CREATE INDEX idx_party_status ON party(status);

-- ============================================================================
-- PARTY_ROLE TABLE (Inherits from PARTY)
-- ============================================================================
CREATE TABLE party_role (
    party_role_id VARCHAR2(50) PRIMARY KEY,
    party_id VARCHAR2(50) NOT NULL,
    party_role_type VARCHAR2(50) NOT NULL,
    role_name VARCHAR2(255) NOT NULL,
    role_description VARCHAR2(1000),
    status VARCHAR2(50) NOT NULL DEFAULT 'ACTIVE',
    status_reason VARCHAR2(500),
    valid_from TIMESTAMP,
    valid_to TIMESTAMP,
    engaged_party_id VARCHAR2(50),
    party_role_specification_id VARCHAR2(50),
    href VARCHAR2(500),
    created_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    created_by VARCHAR2(100),
    updated_by VARCHAR2(100),
    context_data JSON,
    CONSTRAINT fk_party_role_party FOREIGN KEY (party_id) REFERENCES party(party_id),
    CONSTRAINT fk_party_role_engaged FOREIGN KEY (engaged_party_id) REFERENCES party(party_id),
    CONSTRAINT chk_party_role_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PROSPECT'))
);

CREATE INDEX idx_party_role_party_id ON party_role(party_id);
CREATE INDEX idx_party_role_type ON party_role(party_role_type);
CREATE INDEX idx_party_role_status ON party_role(status);

-- ============================================================================
-- CUSTOMER TABLE (Inherits from PARTY_ROLE)
-- ============================================================================
CREATE TABLE customer (
    customer_id VARCHAR2(50) PRIMARY KEY,
    party_role_id VARCHAR2(50) NOT NULL,
    external_id VARCHAR2(100),
    customer_name VARCHAR2(255) NOT NULL,
    email VARCHAR2(255),
    phone VARCHAR2(20),
    cpf_cnpj VARCHAR2(20),
    segment VARCHAR2(100),
    preferred_channel VARCHAR2(50),
    risk_level VARCHAR2(50),
    status VARCHAR2(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    created_by VARCHAR2(100),
    updated_by VARCHAR2(100),
    context_data JSON,
    CONSTRAINT fk_customer_party_role FOREIGN KEY (party_role_id) REFERENCES party_role(party_role_id),
    CONSTRAINT uk_customer_external_id UNIQUE (external_id),
    CONSTRAINT uk_customer_email UNIQUE (email),
    CONSTRAINT uk_customer_cpf_cnpj UNIQUE (cpf_cnpj),
    CONSTRAINT chk_customer_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'PROSPECT', 'SUSPENDED')),
    CONSTRAINT chk_customer_risk_level CHECK (risk_level IN ('LOW', 'MEDIUM', 'HIGH'))
);

CREATE INDEX idx_customer_party_role_id ON customer(party_role_id);
CREATE INDEX idx_customer_external_id ON customer(external_id);
CREATE INDEX idx_customer_email ON customer(email);
CREATE INDEX idx_customer_cpf_cnpj ON customer(cpf_cnpj);
CREATE INDEX idx_customer_status ON customer(status);
CREATE INDEX idx_customer_segment ON customer(segment);
CREATE INDEX idx_customer_risk_level ON customer(risk_level);

-- ============================================================================
-- CONTACT_MEDIUM TABLE (Polimórfica)
-- ============================================================================
CREATE TABLE contact_medium (
    contact_medium_id VARCHAR2(50) PRIMARY KEY,
    party_role_id VARCHAR2(50) NOT NULL,
    contact_type VARCHAR2(50),
    contact_medium_type VARCHAR2(50) NOT NULL,
    preferred NUMBER(1) DEFAULT 0,
    valid_from TIMESTAMP,
    valid_to TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    context_data JSON,
    CONSTRAINT fk_contact_medium_party_role FOREIGN KEY (party_role_id) REFERENCES party_role(party_role_id)
);

CREATE INDEX idx_contact_medium_party_role_id ON contact_medium(party_role_id);
CREATE INDEX idx_contact_medium_type ON contact_medium(contact_medium_type);

-- ============================================================================
-- PHONE_CONTACT_MEDIUM TABLE (Specialization)
-- ============================================================================
CREATE TABLE phone_contact_medium (
    contact_medium_id VARCHAR2(50) PRIMARY KEY,
    phone_number VARCHAR2(20) NOT NULL,
    country_code VARCHAR2(5),
    area_code VARCHAR2(5),
    CONSTRAINT fk_phone_contact_medium FOREIGN KEY (contact_medium_id) REFERENCES contact_medium(contact_medium_id)
);

CREATE INDEX idx_phone_contact_medium_number ON phone_contact_medium(phone_number);

-- ============================================================================
-- EMAIL_CONTACT_MEDIUM TABLE (Specialization)
-- ============================================================================
CREATE TABLE email_contact_medium (
    contact_medium_id VARCHAR2(50) PRIMARY KEY,
    email_address VARCHAR2(255) NOT NULL,
    CONSTRAINT fk_email_contact_medium FOREIGN KEY (contact_medium_id) REFERENCES contact_medium(contact_medium_id)
);

CREATE INDEX idx_email_contact_medium_address ON email_contact_medium(email_address);

-- ============================================================================
-- GEOGRAPHIC_ADDRESS_CONTACT_MEDIUM TABLE (Specialization)
-- ============================================================================
CREATE TABLE geographic_address_contact_medium (
    contact_medium_id VARCHAR2(50) PRIMARY KEY,
    address_line1 VARCHAR2(255),
    address_line2 VARCHAR2(255),
    city VARCHAR2(100),
    state_province VARCHAR2(50),
    postal_code VARCHAR2(20),
    country VARCHAR2(100),
    CONSTRAINT fk_geographic_address_contact_medium FOREIGN KEY (contact_medium_id) REFERENCES contact_medium(contact_medium_id)
);

-- ============================================================================
-- CHARACTERISTIC TABLE
-- ============================================================================
CREATE TABLE characteristic (
    characteristic_id VARCHAR2(50) PRIMARY KEY,
    party_role_id VARCHAR2(50) NOT NULL,
    characteristic_name VARCHAR2(255) NOT NULL,
    characteristic_value VARCHAR2(1000),
    value_type VARCHAR2(50),
    created_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    context_data JSON,
    CONSTRAINT fk_characteristic_party_role FOREIGN KEY (party_role_id) REFERENCES party_role(party_role_id)
);

CREATE INDEX idx_characteristic_party_role_id ON characteristic(party_role_id);
CREATE INDEX idx_characteristic_name ON characteristic(characteristic_name);

-- ============================================================================
-- CREDIT_PROFILE TABLE
-- ============================================================================
CREATE TABLE credit_profile (
    credit_profile_id VARCHAR2(50) PRIMARY KEY,
    party_role_id VARCHAR2(50) NOT NULL,
    credit_profile_date TIMESTAMP,
    credit_risk_rating NUMBER(2),
    credit_score NUMBER(4),
    valid_from TIMESTAMP,
    valid_to TIMESTAMP,
    href VARCHAR2(500),
    created_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_credit_profile_party_role FOREIGN KEY (party_role_id) REFERENCES party_role(party_role_id),
    CONSTRAINT chk_credit_risk_rating CHECK (credit_risk_rating BETWEEN 1 AND 10),
    CONSTRAINT chk_credit_score CHECK (credit_score BETWEEN 0 AND 1000)
);

CREATE INDEX idx_credit_profile_party_role_id ON credit_profile(party_role_id);

-- ============================================================================
-- ACCOUNT_REF TABLE
-- ============================================================================
CREATE TABLE account_ref (
    account_ref_id VARCHAR2(50) PRIMARY KEY,
    party_role_id VARCHAR2(50) NOT NULL,
    account_id VARCHAR2(50) NOT NULL,
    account_name VARCHAR2(255),
    account_href VARCHAR2(500),
    account_type VARCHAR2(50),
    created_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_account_ref_party_role FOREIGN KEY (party_role_id) REFERENCES party_role(party_role_id)
);

CREATE INDEX idx_account_ref_party_role_id ON account_ref(party_role_id);
CREATE INDEX idx_account_ref_account_id ON account_ref(account_id);

-- ============================================================================
-- RELATED_PARTY TABLE
-- ============================================================================
CREATE TABLE related_party (
    related_party_id VARCHAR2(50) PRIMARY KEY,
    party_role_id VARCHAR2(50) NOT NULL,
    related_party_role_id VARCHAR2(50),
    relationship_type VARCHAR2(100),
    valid_from TIMESTAMP,
    valid_to TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_related_party_party_role FOREIGN KEY (party_role_id) REFERENCES party_role(party_role_id),
    CONSTRAINT fk_related_party_related FOREIGN KEY (related_party_role_id) REFERENCES party_role(party_role_id)
);

CREATE INDEX idx_related_party_party_role_id ON related_party(party_role_id);
CREATE INDEX idx_related_party_related_party_role_id ON related_party(related_party_role_id);

-- ============================================================================
-- PAYMENT_METHOD TABLE
-- ============================================================================
CREATE TABLE payment_method (
    payment_method_id VARCHAR2(50) PRIMARY KEY,
    party_role_id VARCHAR2(50) NOT NULL,
    payment_type VARCHAR2(50),
    payment_method_details JSON,
    valid_from TIMESTAMP,
    valid_to TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_payment_method_party_role FOREIGN KEY (party_role_id) REFERENCES party_role(party_role_id)
);

CREATE INDEX idx_payment_method_party_role_id ON payment_method(party_role_id);

-- ============================================================================
-- CUSTOMER_EVENT TABLE (Event Sourcing)
-- ============================================================================
CREATE TABLE customer_event (
    event_id VARCHAR2(50) PRIMARY KEY,
    customer_id VARCHAR2(50) NOT NULL,
    event_type VARCHAR2(100) NOT NULL,
    event_data JSON NOT NULL,
    event_timestamp TIMESTAMP NOT NULL DEFAULT SYSTIMESTAMP,
    actor VARCHAR2(100),
    CONSTRAINT fk_customer_event_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE INDEX idx_customer_event_customer_id ON customer_event(customer_id);
CREATE INDEX idx_customer_event_type ON customer_event(event_type);
CREATE INDEX idx_customer_event_timestamp ON customer_event(event_timestamp);

-- ============================================================================
-- SEQUENCES
-- ============================================================================
CREATE SEQUENCE seq_party_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_party_role_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_customer_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_contact_medium_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_characteristic_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_credit_profile_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_account_ref_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_related_party_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_payment_method_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_customer_event_id START WITH 1 INCREMENT BY 1;

-- ============================================================================
-- COMMENTS
-- ============================================================================
COMMENT ON TABLE party IS 'TMF629 - Party (Individual or Organization)';
COMMENT ON TABLE party_role IS 'TMF629 - Party Role (Role that a party can play)';
COMMENT ON TABLE customer IS 'TMF629 - Customer (Specialization of Party Role)';
COMMENT ON TABLE contact_medium IS 'TMF629 - Contact Medium (Polimórfica)';
COMMENT ON TABLE phone_contact_medium IS 'TMF629 - Phone Contact Medium';
COMMENT ON TABLE email_contact_medium IS 'TMF629 - Email Contact Medium';
COMMENT ON TABLE geographic_address_contact_medium IS 'TMF629 - Geographic Address Contact Medium';
COMMENT ON TABLE characteristic IS 'TMF629 - Characteristic (Dynamic properties)';
COMMENT ON TABLE credit_profile IS 'TMF629 - Credit Profile';
COMMENT ON TABLE account_ref IS 'TMF629 - Account Reference';
COMMENT ON TABLE related_party IS 'TMF629 - Related Party (Relationships)';
COMMENT ON TABLE payment_method IS 'TMF629 - Payment Method';
COMMENT ON TABLE customer_event IS 'TMF629 - Customer Event (Event Sourcing)';

-- ============================================================================
-- COMMIT
-- ============================================================================
COMMIT;
