--wish view
CREATE VIEW wish_view AS
SELECT 
    w.wish_id,
    p.product_id, 
    p.price, 
    p.name, 
    w.owner_name, 
    c.contributor_name, 
    c.amount, 
    SUM(c.amount) OVER (PARTITION BY w.wish_id) AS sum_amount
FROM 
    wish w
INNER JOIN 
    products p ON p.product_id = w.product_id
LEFT JOIN 
    CONTRIBUTION c ON c.wish_id = w.wish_id;