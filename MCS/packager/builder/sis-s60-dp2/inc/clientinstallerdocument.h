/* Copyright (c) 2004, Nokia. All rights reserved */


#ifndef __CLIENTINSTALLERDOCUMENT_H__
#define __CLIENTINSTALLERDOCUMENT_H__

// INCLUDES
#include <akndoc.h>

// FORWARD DECLARATIONS
class CCLIENTINSTALLERAppUi;
class CEikApplication;


// CLASS DECLARATION

/**
* CCLIENTINSTALLERDocument application class.
* An instance of class CCLIENTINSTALLERDocument is the Document part of the
* AVKON application framework for the CLIENTINSTALLER example application.
*/
class CClientInstallerDocument : public CAknDocument
    {
    public: // Constructors and destructor

        /**
        * NewL.
        * Two-phased constructor.
        * Construct a CCLIENTINSTALLERDocument for the AVKON application aApp
        * using two phase construction, and return a pointer
        * to the created object.
        * @param aApp Application creating this document.
        * @return A pointer to the created instance of CCLIENTINSTALLERDocument.
        */
        static CClientInstallerDocument* NewL( CEikApplication& aApp );

        /**
        * NewLC.
        * Two-phased constructor.
        * Construct a CCLIENTINSTALLERDocument for the AVKON application aApp
        * using two phase construction, and return a pointer
        * to the created object.
        * @param aApp Application creating this document.
        * @return A pointer to the created instance of CCLIENTINSTALLERDocument.
        */
        static CClientInstallerDocument* NewLC( CEikApplication& aApp );

        /**
        * ~CCLIENTINSTALLERDocument
        * Virtual Destructor.
        */
        virtual ~CClientInstallerDocument();

    public: // Functions from base classes

        /**
        * CreateAppUiL
        * From CEikDocument, CreateAppUiL.
        * Create a CCLIENTINSTALLERAppUi object and return a pointer to it.
        * The object returned is owned by the Uikon framework.
        * @return Pointer to created instance of AppUi.
        */
        CEikAppUi* CreateAppUiL();

    private: // Constructors

        /**
        * ConstructL
        * 2nd phase constructor.
        */
        void ConstructL();

        /**
        * CCLIENTINSTALLERDocument.
        * C++ default constructor.
        * @param aApp Application creating this document.
        */
        CClientInstallerDocument( CEikApplication& aApp );

    };

#endif // __CLIENTINSTALLERDOCUMENT_H__

// End of File