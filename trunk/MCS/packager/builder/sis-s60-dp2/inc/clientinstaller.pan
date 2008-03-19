/* Copyright (c) 2004, Nokia. All rights reserved */


#ifndef __CLIENTINSTALLER_PAN__
#define __CLIENTINSTALLER_PAN__

/** ClientInstaller application panic codes */
enum TClientInstallerPanics
    {
    EClientInstallerUi = 1
    // add further panics here
    };

inline void Panic(TClientInstallerPanics aReason)
    {
    _LIT(applicationName,"ClientInstaller");
    User::Panic(applicationName, aReason);
    }

#endif // __CLIENTINSTALLER_PAN__
