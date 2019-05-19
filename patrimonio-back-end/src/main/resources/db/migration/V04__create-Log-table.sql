CREATE TABLE [dbo].[Log](
	[log_id] [bigint] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[date] [datetime] NOT NULL,
	[interfacename] [varchar](100) NOT NULL,
	[action] [varchar](100) NOT NULL,
	[user_id] [varchar](85) NOT NULL
);