CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation VARCHAR(100) NOT NULL,
    result VARCHAR(255) NOT NULL,
    measurement_type VARCHAR(100),
    is_error BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS quantity_measurement_history (
    history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation VARCHAR(100) NOT NULL,
    result VARCHAR(255) NOT NULL,
    measurement_type VARCHAR(100),
    is_error BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_qm_entity_operation
    ON quantity_measurement_entity(operation);

CREATE INDEX IF NOT EXISTS idx_qm_entity_measurement_type
    ON quantity_measurement_entity(measurement_type);

CREATE INDEX IF NOT EXISTS idx_qm_history_operation
    ON quantity_measurement_history(operation);
