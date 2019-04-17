CREATE TABLE [dbo].[User](
	[user_id] [bigint] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[name] [varchar](50) NOT NULL,
	[username] [varchar](30) NOT NULL,
	[password] [varchar](40) NOT NULL,
	[userlevel] [int] NOT NULL,
	[status] [bit] NOT NULL
);