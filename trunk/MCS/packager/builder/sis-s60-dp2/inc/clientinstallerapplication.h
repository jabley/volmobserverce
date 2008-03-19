


#ifndef __CLIENTINSTALLERAPPLICATION_H__
#define __CLIENTINSTALLERAPPLICATION_H__

// INCLUDES
#include <aknapp.h>
#include <apgcli.h> // apgrfx.lib
#include <apgtask.h>
#include <EIKENV.h>

// CLASS DECLARATION

/**
* CCLIENTINSTALLERApplication application class.
* Provides factory to create concrete document object.
* An instance of CCLIENTINSTALLERApplication is the application part of the
* AVKON application framework for the CLIENTINSTALLER example application.
*/
class CClientInstallerApplication : public CAknApplication
    {
    public: // Functions from base classes

        /**
        * From CApaApplication, AppDllUid.
        * @return Application's UID (KUidCLIENTINSTALLERApp).
        */
        TUid AppDllUid() const;

        /**
        * This method stars a Netfront browser using the URL specified
        * as a parameter.
        */
        void StartBrowser(const TDesC&);

		/**
		* This method checks on the existance of the file specified using
		* the file system available on the device.
		* @return true/false, depending on the existance of the file
		*/
        bool FileExists(const TDesC&);


    protected: // Functions from base classes

        /**
        * From CApaApplication, CreateDocumentL.
        * Creates CCLIENTINSTALLERDocument document object. The returned
        * pointer in not owned by the CCLIENTINSTALLERApplication object.
        * @return A pointer to the created document object.
        */
        CApaDocument* CreateDocumentL();
    };

#endif // __CLIENTINSTALLERAPPLICATION_H__

// End of File