# MonkeySimulator
Tool encapsulating monkey command for performance and pressure testing on Android apps

Current version is only used for **BlackBerry** apps.

**Version 1.0**

1. Fetch all BlackBerry apps first(Both apps and services), displaying as app name with current version.
2. Select one from app list and entry Monkey parameteres.
3. Click Start to execute Monkey command.
4. Result displays in report page.
5. Go back to app list or save the report to SD card for further analysis.


**Version 1.1**

1. Display the entry value at para list after press OK.
2. Reconstructure part of methods to decrease coupling.
  
**Version 1.3**


1. Separate all Strings inside the code.
2. Be able to save input values for next time convenience.

**Bug:**
    
    1. Inputing empty value on optional paras works fine on KeyOne but doesn't on DTEK60. 
    
**Version 1.4**

1. Add Category para.

**Version 2.0**

1. Add button to control Monkey during execution.

**Version 2.1**

1. Add filter to the final result display. eg. Exceptions, errors, crashes.
