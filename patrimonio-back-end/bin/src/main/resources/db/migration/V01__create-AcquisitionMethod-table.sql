CREATE TABLE [dbo].[AcquisitionMethod](
	[acquisition_method_id] [bigint] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[description] [varchar](50) NOT NULL UNIQUE
);