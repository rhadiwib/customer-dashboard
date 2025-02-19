# Project_Structure
```src_main_project_structure
> customer-dashboard/src/main/
├──java
│   └──com
│       └──regional
│           └──customerdashboard
│              ├──CustomerDashboardApplication.java
│              ├──controller
│              │  └──ManagerController.java
│              ├──domain
│              │  ├──entity
│              │  │   ├──Customer.java
│              │  │   ├──Manager.java
│              │  │   └──Region.java
│              │  └──enums
│              │      └──RegionType.java
│              ├──error
│              │  └──GlobalExceptionHandler.java
│              ├──repository
│              │  ├──CustomerRepository.java
│              │  ├──ManagerRepository.java
│              │  └──RegionRepository.java
│              └──service
│                 ├──dto
│                 │  ├──CustomerDTO.java
│                 │  ├──ManagerDetailsDTO.java
│                 │  └──ManagerDTO.java
│                 └──ManagerService.java
└──resources
   ├──application.yml
   ├──static
   └──templates

15 directories, 15 files
```