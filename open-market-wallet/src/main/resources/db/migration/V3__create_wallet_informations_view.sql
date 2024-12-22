create or replace view wallet_informations_view as
select wlt.wlt_id as id,
       wlt.wlt_updated_at as last_update,
       (
           select coalesce(sum(
                                   case
                                       when (wlg2.wlg_wallet_id = wlt.wlt_id or wlg2.wlg_transaction_type in ('WITHDRAWAL')) then -wlg2.wlg_value
                                       when wlg2.wlg_target_wallet_id = wlt.wlt_id then wlg2.wlg_value
                                       end
                           ), 0) from tb_wallets_ledgers wlg2
           where (wlg2.wlg_target_wallet_id = wlt.wlt_id or wlg2.wlg_wallet_id = wlt.wlt_id)
       )  as current_balance
from tb_wallets wlt;