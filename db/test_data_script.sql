INSERT INTO Category (CategoryName)
VALUES 
    ('Work'),
    ('Personal'),
    ('Urgent'),
    ('Home Improvement')
ON CONFLICT (CategoryName) DO NOTHING;

INSERT INTO Task (Title, TaskDesc, DueDate, Completed, CategoryId)
VALUES 
    ('Submit Q1 Report', 'Finalize the revenue spreadsheet', CURRENT_DATE + 2, FALSE, 2),
    ('Buy Groceries', 'Milk, Eggs, Bread, and Coffee', CURRENT_DATE, TRUE, 3),
    ('Fix Kitchen Sink', NULL, CURRENT_DATE - 1, FALSE, 5),
    ('Call Insurance', 'Discuss renewal options', CURRENT_DATE + 7, FALSE, 4),
    ('Walk the Dog', 'Take the long route through the park', CURRENT_DATE, FALSE, 1),
    ('Gym Session', NULL, CURRENT_DATE + 1, FALSE, 1);

