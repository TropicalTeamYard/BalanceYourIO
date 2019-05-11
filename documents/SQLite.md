# byio.db

```
账本{
    _id integer
    标签 text
    时间 text
    收支类型 integer {
        Income
        Outcome
        Adjust
    }
    种类 text {
        默认值数字枚举
        用户自定义 枚举
    }
    数额 double
    支付渠道 text {
        唯一标识枚举
    }
    备注 text
}
```

# usr_conf.json

```
用户设置{
    ...
}
```

sajdgkj