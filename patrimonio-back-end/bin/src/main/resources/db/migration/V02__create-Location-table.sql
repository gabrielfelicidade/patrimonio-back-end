CREATE TABLE [dbo].[Location](
	[location_id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[description] [varchar](100) NOT NULL,
	[status] [bit] NOT NULL
);