CREATE TABLE [dbo].[Patrimony](
	[patrimony_id] [int] NOT NULL PRIMARY KEY,
	[acquisition_process_id] [varchar](20) NULL,
	[serial_number] [varchar](15) NULL,
	[description] [varchar](100) NOT NULL,
	[commercial_invoice] [varchar](20) NULL,
	[model] [varchar](50) NULL,
	[brand] [varchar](50) NULL,
	[additional_information] [varchar](200) NULL,
	[value] [decimal](10, 2) NULL,
	[location_id] [int] NULL FOREIGN KEY REFERENCES Location(location_id),
	[acquisition_method_id] [int] NULL FOREIGN KEY REFERENCES AcquisitionMethod(acquisition_method_id)
);