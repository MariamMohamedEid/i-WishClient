--wish view
CREATE or replace VIEW wish_view AS
SELECT 
    w.wish_id,
    p.product_id, 
    p.price, 
    p.name, 
    w.owner_name, 
    c.contributor_name, 
    c.amount, 
    nvl(SUM(c.amount) OVER (PARTITION BY w.wish_id),0) AS sum_amount,
    p.price - nvl(SUM(c.amount) OVER (PARTITION BY w.wish_id),0) AS remaining_amount
FROM 
    wish w
INNER JOIN 
    products p ON p.product_id = w.product_id
LEFT JOIN 
    contribution c ON c.wish_id = w.wish_id;