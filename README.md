PlaylistMaker - приложение, позволяющее искать треки в базе iTunes по введённому запросу
Возможности приложения:
поиск по названию трека
поиск по исполнителю
ознакомительного прослушивание
создания плейлистов из треков
хранение спасибо плейлистов в БД на устройстве
добавления треков в список избранного
хранения списка избранных треков в БД на устройстве
Реализация:
шаблон проектирования MVVM
подход SingleActivity
БД организована с помощью Room
сетевые функции приложения реализованы с помощью Retrofit
Инструменты разработки:
Retrofit, Room, Glide, GSON, Koin, JetpackNavigation, JSON, Fragments, XML, BottomNavigation, ViewPager2

Частичное руководство по возможностям приложения:
Экран поиска
Окно поиска
История поиска, при пустой истории выведется сообщение-заглушка
Кнопка очистки истории поиска, при пустой истории не отображается
[PreSearchEdited](https://private-user-images.githubusercontent.com/144613563/390008359-fb40c253-ef9c-40ec-b30c-c558f7209c51.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDAyNDY5NTIsIm5iZiI6MTc0MDI0NjY1MiwicGF0aCI6Ii8xNDQ2MTM1NjMvMzkwMDA4MzU5LWZiNDBjMjUzLWVmOWMtNDBlYy1iMzBjLWM1NThmNzIwOWM1MS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwMjIyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDIyMlQxNzUwNTJaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1kMTNlYzFiZDdhYjA3MDk3OGIzOTE2MzlmNDllYjBmNDI4ODRhNGE5ODIxMDRmZDI2MmU5YWZmMTgwYWNiMTYzJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.ZA5TUIpa4VoW0MBPVTVtfqfIg4AR47JFEunSQCNy0KE)

Экран результатов поиска
Окно поиска
Список найденных треков
Элемент списка, при нажатии открывается плеер, добавляет этот элемент в историю поиска
BottomNavigation с разделами приложения
SearchScreenEdited

Экран плеера
Обложка альбома
Подробная информация о треке
Добавление трека в плейлист
Запуск/остановка воспроизведения
Добавление в избранное
PlayerScreenEdited

Экран медиатеки
Страница со списом избрынных треков
Страница со списком плейлистов
Общая вкладка в BottomNavigationView. Для страниц использован ViewPager2
FavoriteViewEdited

Экран создания плейлиста
Добавление обложки из галереи
Название плейлиста - без названия плейлист создан не будет
Описание плейлиста - может быть пустым
Завершение создания плейлиста
PlaylistCreatingEdited
