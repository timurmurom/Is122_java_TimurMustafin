#!/bin/sh

# Название файла базы
DB_NAME="red_test.fdb"

# Путь к файлу базы
if [ "$(uname)" = "Linux" ]; then
  DB_PATH="/tmp/$DB_NAME"
else
  # Windows через Git Bash — используем текущую директорию
  DB_PATH="$(pwd)/$DB_NAME"
fi

# Логин и пароль (по умолчанию SYSDBA/masterkey)
DB_USER="SYSDBA"
DB_PASSWORD="masterkey"

# Путь к isql (можно заменить на полный путь если не в $PATH)
ISQL="isql"

# Команда создания базы
CREATE_SQL="CREATE DATABASE '$DB_PATH' USER '$DB_USER' PASSWORD '$DB_PASSWORD';"

# Выполняем
echo "$CREATE_SQL" | $ISQL

# Проверка
if [ $? -eq 0 ]; then
  echo "✅ База данных создана: $DB_PATH"
else
  echo "❌ Ошибка при создании базы данных"
fi
