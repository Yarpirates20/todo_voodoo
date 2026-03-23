DROP TABLE IF EXISTS Task;
DROP TABLE IF EXISTS Category;

-- Create Category table
CREATE TABLE IF NOT EXISTS Category(
	CategoryId	INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	CategoryName TEXT NOT NULL UNIQUE

	
);

-- Create Task table
CREATE TABLE IF NOT EXISTS Task(
	TaskId		INT	GENERATED  ALWAYS AS IDENTITY   PRIMARY KEY,
	CreatedOn	TIMESTAMPTZ DEFAULT NOW(),
	Title       TEXT NOT NULL,
	TaskDesc	TEXT,
	DueDate		DATE ,
	Completed	BOOLEAN NOT NULL DEFAULT FALSE,
	CategoryId	INT REFERENCES Category(CategoryId) ON DELETE CASCADE
);

-- Insert 'Uncategorized' category with 1 as CategoryId.
INSERT INTO Category (CategoryId, CategoryName)
	OVERRIDING SYSTEM VALUE
	VALUES (1, 'Uncategorized')
	ON CONFLICT (CategoryId) DO NOTHING;

-- To avoid sync errors
SELECT setval(pg_get_serial_sequence('"category"', 'categoryid'), max(categoryid)) FROM Category;