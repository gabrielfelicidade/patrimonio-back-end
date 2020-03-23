CREATE TABLE patrimony (
	patrimony_id bigint NOT NULL PRIMARY KEY,
	acquisition_process_id varchar(20) NULL,
	serial_number varchar(15) NULL,
	description varchar(100) NOT NULL,
	commercial_invoice varchar(20) NULL,
	model varchar(50) NULL,
	brand varchar(50) NULL,
	additional_information varchar(200) NULL,
	value decimal(10, 2) NULL,
	status int NOT NULL DEFAULT 2,
	writeoff_date date NULL,
	location_id bigint NULL,
	acquisition_method_id bigint NULL,
	CONSTRAINT fk_location FOREIGN KEY (location_id)
	REFERENCES location(location_id),
	CONSTRAINT fk_acquisitionMethod FOREIGN KEY (acquisition_method_id)
	REFERENCES acquisitionmethod(acquisition_method_id)
);
