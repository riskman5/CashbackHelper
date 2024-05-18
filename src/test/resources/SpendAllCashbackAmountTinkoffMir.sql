UPDATE cards
SET earned_cashback_amount = 5000, remaining_cashback_amount = 0
WHERE name = 'Tinkoff Mir';
UPDATE cashbacks

SET remaining_cashback_amount = 0
WHERE card_id = (SELECT id FROM cards WHERE name = 'Tinkoff Mir');