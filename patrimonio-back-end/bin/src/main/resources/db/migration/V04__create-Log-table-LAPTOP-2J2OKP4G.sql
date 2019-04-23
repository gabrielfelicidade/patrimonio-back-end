CREATE TABLE [dbo].[Log](
	[log_id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[date] [date] NOT NULL,
	[tablename] [varchar](25) NOT NULL,
	[action] [varchar](10) NOT NULL,
	[user_id] [int] NOT NULL FOREIGN KEY REFERENCES [User](user_id)
);