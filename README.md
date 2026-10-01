# 💊 MediCareAlert

**MediCareAlert** เป็นแอปพลิเคชันบนระบบปฏิบัติการ Android ที่พัฒนาขึ้นเพื่อช่วยให้ผู้ใช้งานสามารถจัดการข้อมูลเกี่ยวกับยา บันทึกข้อมูลสุขภาพ และติดตามการดูแลสุขภาพของตนเองได้อย่างเป็นระบบ

โปรเจกต์นี้จัดทำขึ้นเป็นส่วนหนึ่งของรายวิชา **Mobile App Development** ในขณะที่ศึกษาอยู่ **ชั้นปีที่ 2** โดยมีวัตถุประสงค์เพื่อฝึกกระบวนการพัฒนา Mobile Application ตั้งแต่การออกแบบหน้าจอ การจัดการ Navigation การพัฒนาฟังก์ชันภายในแอป ไปจนถึงการจัดโครงสร้างโปรเจกต์สำหรับ Android Application

> **หมายเหตุ:** Repository นี้จัดทำขึ้นเพื่อการศึกษา และนำมาเผยแพร่บน GitHub เพื่อใช้เป็นส่วนหนึ่งของ Portfolio ด้าน Mobile Application Development

---

## 📱 เกี่ยวกับแอปพลิเคชัน

MediCareAlert ถูกออกแบบมาเพื่อช่วยผู้ใช้งานจัดการข้อมูลด้านสุขภาพในชีวิตประจำวัน โดยเน้นการใช้งานที่เข้าใจง่าย และรวบรวมฟังก์ชันสำคัญไว้ภายในแอปเดียว

ผู้ใช้งานสามารถบันทึกข้อมูลยา กำหนดช่วงเวลาการรับประทานยา บันทึกอาการหรือข้อมูลสุขภาพ และเชื่อมต่อกับผู้ดูแลหรือบุคคลใกล้ชิดเพื่อช่วยติดตามการดูแลสุขภาพ

---

## ✨ ฟังก์ชันหลักของระบบ

### 💊 Medication Tracker & Reminders

ระบบจัดการและติดตามการรับประทานยา

- เพิ่มข้อมูลยา
- กำหนดตารางเวลาในการรับประทานยา
- จัดการข้อมูลยาที่ใช้งาน
- รองรับการแจ้งเตือนเพื่อช่วยลดการลืมรับประทานยา

### 🩺 Health Diary & Records

ระบบบันทึกข้อมูลสุขภาพส่วนบุคคล

- บันทึกอาการหรือข้อมูลสุขภาพในแต่ละช่วงเวลา
- เก็บประวัติข้อมูลสุขภาพ
- ช่วยให้ผู้ใช้งานสามารถติดตามข้อมูลสุขภาพย้อนหลังได้

### 👨‍👩‍👧 Caregiver & Friends Network

ระบบเชื่อมต่อกับผู้ดูแลหรือบุคคลใกล้ชิด

- เชื่อมต่อกับสมาชิกในครอบครัวหรือผู้ดูแล
- ใช้สำหรับติดตามหรือแบ่งปันข้อมูลที่เกี่ยวข้องกับสุขภาพ
- ช่วยเพิ่มความสะดวกในการดูแลผู้ใช้งาน

### 🔐 User Authentication

ระบบสมาชิกสำหรับรักษาความเป็นส่วนตัวของข้อมูล

- สมัครสมาชิก
- เข้าสู่ระบบ
- แยกข้อมูลตามบัญชีผู้ใช้งาน

### 🎨 User Interface

ออกแบบส่วนติดต่อผู้ใช้งานโดยใช้ Android UI Components และ XML Layout

- ใช้ ViewBinding ในการเชื่อมต่อ UI กับ Source Code
- ใช้ Navigation Component สำหรับควบคุมการเปลี่ยนหน้าภายในแอป
- ออกแบบหน้าจอให้สามารถใช้งานได้ง่ายบนอุปกรณ์ Android

---

## 🛠️ เทคโนโลยีที่ใช้

### Programming Language

- Kotlin
- Java

### Android Development

- Android SDK
- Android View System
- XML Layout
- ViewBinding

### Navigation

- Jetpack Navigation Component

### Development Environment

- Android Studio
- Gradle Kotlin DSL

### Android Version

- **Minimum SDK:** 26 — Android 8.0
- **Target SDK:** 34 — Android 14

---

## 🗂️ โครงสร้างโปรเจกต์

```text
app/
└── src/
    └── main/
        ├── java/
        │   └── com/example/medicarealert/
        │       ├── Activities
        │       ├── Fragments
        │       ├── Adapters
        │       └── Models
        │
        └── res/
            ├── layout/          # XML Layout สำหรับหน้าจอต่าง ๆ
            ├── navigation/      # Navigation Graph ของแอป
            ├── drawable/        # รูปภาพและ UI Resources
            └── values/          # Colors, Strings และ Themes
```

---

## 🚀 วิธีติดตั้งและทดลองใช้งาน

### 1. Clone Repository

```bash
git clone https://github.com/yourusername/MediCareAlert.git
```

### 2. เปิดโปรเจกต์

เปิดโปรเจกต์ด้วย **Android Studio**

### 3. Sync Gradle

รอให้ Android Studio ดาวน์โหลด Dependencies และทำการ Gradle Sync ให้เสร็จสมบูรณ์

### 4. Run Application

สามารถทดลองใช้งานผ่าน

- Android Emulator
- Android Smartphone

จากนั้นกด **Run** ภายใน Android Studio เพื่อเปิดแอปพลิเคชัน

---

## 📸 Screenshots

สามารถเพิ่มภาพตัวอย่างหน้าจอของแอปพลิเคชันในส่วนนี้

### Home Dashboard

<!-- เพิ่มภาพหน้า Home Dashboard -->

### Add Medication

<!-- เพิ่มภาพหน้าเพิ่มข้อมูลยา -->

### Health Record

<!-- เพิ่มภาพหน้าบันทึกข้อมูลสุขภาพ -->

### Login / Register

<!-- เพิ่มภาพหน้าเข้าสู่ระบบหรือสมัครสมาชิก -->

---

## 🎯 สิ่งที่ได้รับจากโปรเจกต์

โปรเจกต์นี้ช่วยให้ได้เรียนรู้และฝึกกระบวนการพัฒนา Mobile Application บน Android ตั้งแต่พื้นฐานจนสามารถสร้างแอปพลิเคชันที่มีหลายหน้าจอและมีฟังก์ชันการทำงานร่วมกันได้

สิ่งที่ได้เรียนรู้จากการพัฒนาโปรเจกต์ ได้แก่

- การพัฒนา Android Application ด้วย Kotlin และ Java
- การออกแบบหน้าจอด้วย XML Layout
- การใช้งาน Activity และ Fragment
- การใช้ ViewBinding
- การใช้งาน Jetpack Navigation Component
- การจัดการ Navigation ระหว่างหน้าจอ
- การออกแบบโครงสร้าง Mobile Application
- การจัดการข้อมูลภายในแอปพลิเคชัน
- การทดสอบแอปผ่าน Android Emulator และอุปกรณ์จริง
- การใช้งาน Android Studio และ Gradle

---

## 👩‍💻 Developer

**Aminta Rungruang (อมินตา รุ่งเรือง)**

Computer Science Student  
Phetchaburi Rajabhat University

**Project:** MediCareAlert  
**Course:** Mobile App Development  
**Academic Year:** 2nd Year

โปรเจกต์นี้จัดทำขึ้นในขณะที่ศึกษาอยู่ **ชั้นปีที่ 2** เพื่อประยุกต์ใช้ความรู้ด้านการพัฒนา Mobile Application บนระบบ Android และฝึกกระบวนการออกแบบและพัฒนาแอปพลิเคชันตั้งแต่ต้นจนสามารถใช้งานได้

---

## 📄 Disclaimer

โปรเจกต์นี้จัดทำขึ้นเพื่อวัตถุประสงค์ทางการศึกษา และใช้เป็นส่วนหนึ่งของ **Portfolio** เพื่อแสดงทักษะด้าน Mobile Application Development

ข้อมูลภายในแอปพลิเคชันใช้สำหรับการสาธิตระบบเท่านั้น และไม่ได้มีวัตถุประสงค์เพื่อใช้แทนคำแนะนำ การวินิจฉัย หรือการรักษาจากบุคลากรทางการแพทย์
