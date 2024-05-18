UPDATE cards
SET earned_cashback_amount = 1000, remaining_cashback_amount = 0
WHERE name = 'Sberbank Visa';
UPDATE cashbacks

SET remaining_cashback_amount = 0
WHERE card_id = (SELECT id FROM cards WHERE name = 'Sberbank Visa');