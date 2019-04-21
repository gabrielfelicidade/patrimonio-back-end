CREATE TABLE [dbo].[User](
	[user_id] [bigint] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[name] [varchar](50) NOT NULL,
	[username] [varchar](30) NOT NULL UNIQUE,
	[password] [varchar](60) NOT NULL,
	[userlevel] [int] NOT NULL
);