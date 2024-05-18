INSERT INTO cashbacks(card_id, cashback_percentage, category,
                      is_permanent, remaining_cashback_amount, start_date, end_date)
VALUES ((SELECT id FROM cards WHERE name = 'Sberbank Visa'), 2, 'Развлечения',
        true, 1000, '2024-05-01', '2024-06-01');

