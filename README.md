## 普通预售通过该作业，在预售开始时，同步到poststock.poststock_queue表中，
## 库存同步作业会将数据同步至主表，预售正式开始

## 同步数据选择范围是预售表中是预售(warehouse_type=1)&&未同步至主表(update_status=0)&&
## 到了预售开始时间(event_start_time<=now)&&是普通预售的数据(pre_stock_type=0)
## 

