# 提供一个访问 Mybatis Migration 可执行文件的快捷方式

if [ -z "$DB_NAME" ]; then
    # shellcheck disable=SC2046
    export $(cat .env)
fi

# 数据库 schema

export DB_SCHEMA=break_up_ledger

cd ./migrate/repository || exit

# shellcheck disable=SC2068
../bin/migrate $@