INSERT INTO cards(earned_cashback_amount, name, remaining_cashback_amount, bank_id)
values
    (0, 'Tinkoff Mir', 5000, (SELECT id FROM banks WHERE name = 'Tinkoff'));