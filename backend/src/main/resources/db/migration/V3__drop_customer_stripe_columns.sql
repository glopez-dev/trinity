-- The Stripe OAuth linkage on customers was never read by any code path; the
-- payments table (V2) is now the payment-side source of truth. Dropping the
-- columns removes a payment concern from the user module.
alter table customer drop column stripe_user_id;
alter table customer drop column stripe_access_token;
alter table customer drop column stripe_refresh_token;
alter table customer drop column token_expires_at;
