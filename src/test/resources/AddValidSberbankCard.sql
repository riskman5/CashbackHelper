INSERT INTO cards(earned_cashback_amount, name, remaining_cashback_amount, bank_id)
values
    (0, 'Sberbank Visa', 1000, (SELECT id FROM banks WHERE name = 'Sberbank'));
