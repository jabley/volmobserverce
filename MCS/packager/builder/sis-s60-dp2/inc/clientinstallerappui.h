/* Copyright (c) 2004, Nokia. All rights reserved */


#ifndef __CLIENTINSTALLERAPPUI_H__
#define __CLIENTINSTALLERAPPUI_H__

// INCLUDES
#include <aknappui.h>


// FORWARD DECLARATIONS
class CClientInstallerAppView;


// CLASS DECLARATION
/**
* CCLIENTINSTALLERAppUi application UI class.
* Interacts with the user through the UI and request message processing
* from the handler class
*/
class CClientInstallerAppUi : public CAknAppUi
    {
    public: // Constructors and destructor

        /**
        * ConstructL.
        * 2nd phase constructor.
        */
        void ConstructL();

        /**
        * CCLIENTINSTALLERAppUi.
        * C++ default constructor. This needs to be public due to
        * the way the framework constructs the AppUi
        */
        CClientInstallerAppUi();

        /**
        * ~CCLIENTINSTALLERAppUi.
        * Virtual Destructor.
        */
        virtual ~CClientInstallerAppUi();

    public:  // Functions from base classes

        /**
        * From CEikAppUi, HandleCommandL.
        * Takes care of command handling.
        * @param aCommand Command to be handled.
        */
        void HandleCommandL( TInt aCommand );

    private: // Data

        /**
        * The application view
        * Owned by CCLIENTINSTALLERAppUi
        */
        CClientInstallerAppView* iAppView;
    };

#endif // __CLIENTINSTALLERAPPUI_H__

// End of File