# БизМап — Business Directory Android App

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_app_logo.png" width="100" alt="БизМап Logo"/>
</p>

<p align="center">
  <b>Апликација за прелистување на бизниси поделени по категории</b><br/>
  Развиена за предметот <i>Развој на мобилни апликации</i>
</p>

---

## Автор

| Име и Презиме | Индекс |
|---------------|--------|
| Радмила Лазарова | 102758 |

---

## Опис

**БизМап** е Android апликација која претставува бизнис директориум со компании поделени во четири категории: Сервиси, Забава, Индустрија и Едукација. Апликацијата комуницира со оддалечена база на податоци преку REST API и поддржува работа во офлајн режим.

---

## Функционалности

- **TabLayout + ViewPager2** — четири категории со swipe навигација
- **ListView** — приказ на компании со лого, адреса, телефон и веб страна
- **Пребарување во реално време** — филтрирање по назив во рамките на категоријата
- **Додавање компанија** — форма со сите полиња и избор на категории (checkbox)
- **Оддалечена база** — податоците се зачувуваат на MySQL сервер преку PHP REST API
- **Офлајн режим** — Room локален кеш при нема интернет
- **GPS геолокација** — Toast порака кога корисникот е на помалку од 50м до компанија

---

## Технологии

### Android (Frontend)
| Технологија | Верзија | Улога |
|-------------|---------|-------|
| Android Studio | Hedgehog+ | IDE |
| Java | 11 | Програмски јазик |
| Retrofit 2 | 2.9.0 | HTTP клиент за REST API |
| OkHttp | 4.11.0 | Мрежно логирање |
| Room | 2.6.1 | Локална SQLite база (кеш) |
| Picasso | 2.8 | Вчитување на слики |
| ViewPager2 | 1.0.0 | Swipe меѓу табови |
| Material Design | 1.11.0 | UI компоненти |
| CardView | 1.0.0 | Картички во листата |

### Backend (Server)
| Технологија | Улога |
|-------------|-------|
| PHP 8.2 | REST API |
| MySQL | База на податоци |
| Apache (XAMPP) | Веб сервер |
| PDO | Безбедни SQL прашања |

---

## Архитектура

```
com.radmila.businessdirectory/
├── activity/
│   ├── MainActivity.java          ← Главен екран (TabLayout)
│   └── AddCompanyActivity.java    ← Форма за додавање компанија
├── adapter/
│   ├── CategoryPagerAdapter.java  ← ViewPager2 адаптер
│   └── CompanyAdapter.java        ← ListView адаптер
├── database/
│   ├── AppDatabase.java           ← Room singleton
│   ├── CompanyDao.java            ← SQL прашања
│   └── CompanyEntity.java         ← Room табела
├── fragment/
│   └── CategoryFragment.java      ← Еден таб
├── location/
│   └── LocationHelper.java        ← GPS пресметки
├── model/
│   └── Company.java               ← POJO модел
└── network/
    ├── ApiClient.java             ← Retrofit singleton
    ├── ApiResponse.java           ← Серверски одговор
    └── ApiService.java            ← REST endpoints
```

---

## REST API Endpoints

| Метод | URL | Опис |
|-------|-----|------|
| GET | `/companies.php` | Сите компании |
| GET | `/companies.php?category=services` | По категорија |
| GET | `/companies.php?category=fun&search=кафе` | Пребарување |
| POST | `/companies.php` | Додај нова компанија |

### Пример POST тело (JSON):
```json
{
  "name": "Пример Компанија",
  "address": "ул. Пример 1, Штип",
  "latitude": 41.7457,
  "longitude": 22.1954,
  "email": "info@primer.mk",
  "phone": "+389 77 123 456",
  "website": "www.primer.mk",
  "categories": "services,industry"
}
```

---

## Поставување

### Предуслови
- Android Studio (Hedgehog или понова)
- XAMPP (Apache + MySQL)
- Android уред или емулатор (API 24+)

### Backend
```bash
1. Стартувај XAMPP → Apache + MySQL
2. Копирај ја папката `business_api/` во `htdocs/`
3. Отвори phpMyAdmin → New Database → `business_directory`
4. Увези го `database/schema.sql`
```

### Android
```bash
1. Отвори го проектот во Android Studio
2. Во `ApiClient.java` постави ја IP адресата:
   - Емулатор:       http://10.0.2.2/business_api/
   - Вистински уред: http://192.168.X.X/business_api/
3. Build → Run
```

---

## База на податоци

```sql
CREATE TABLE companies (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    address    VARCHAR(500),
    latitude   DECIMAL(10,8) DEFAULT 0,
    longitude  DECIMAL(11,8) DEFAULT 0,
    email      VARCHAR(255),
    phone      VARCHAR(50),
    website    VARCHAR(255),
    logo_url   VARCHAR(500),
    categories VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## Референци

- [Retrofit](https://square.github.io/retrofit/)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Picasso](https://github.com/square/picasso)
- [Material Components](https://material.io/develop/android)
- [ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2)

---

## Лиценца

Овој проект е изработен за образовни цели во рамките на предметот  
**Развој на мобилни апликации**  Универзитет Гоце Делчев, Штип.
