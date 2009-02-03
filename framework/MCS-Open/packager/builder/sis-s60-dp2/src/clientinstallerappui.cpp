

// INCLUDE FILES
#include <avkon.hrh>
#include <aknnotewrappers.h>
#include <stringloader.h>
#include <ClientInstaller.rsg>

#include "ClientInstaller.pan"
#include "ClientInstallerAppUi.h"
#include "ClientInstallerAppView.h"
#include "ClientInstaller.hrh"

// ============================ MEMBER FUNCTIONS ===============================


// -----------------------------------------------------------------------------
// CClientInstallerAppUi::ConstructL()
// Symbian 2nd phase constructor can leave.
// -----------------------------------------------------------------------------
//
void CClientInstallerAppUi::ConstructL()
    {
    // Initialise app UI with standard value.
    BaseConstructL();

    // Create view object
    iAppView = CClientInstallerAppView::NewL( ClientRect() );
    }

// -----------------------------------------------------------------------------
// CClientInstallerAppUi::CClientInstallerAppUi()
// C++ default constructor can NOT contain any code, that might leave.
// -----------------------------------------------------------------------------
//
CClientInstallerAppUi::CClientInstallerAppUi()
    {
    // No implementation required
    }

// -----------------------------------------------------------------------------
// CClientInstallerAppUi::~CClientInstallerAppUi()
// Destructor.
// -----------------------------------------------------------------------------
//
CClientInstallerAppUi::~CClientInstallerAppUi()
    {
    if ( iAppView )
        {
        delete iAppView;
        iAppView = NULL;
        }
    }

// -----------------------------------------------------------------------------
// CClientInstallerAppUi::HandleCommandL()
// Takes care of command handling.
// -----------------------------------------------------------------------------
//
void CClientInstallerAppUi::HandleCommandL( TInt aCommand )
    {
    switch( aCommand )
        {
        case EEikCmdExit:
        case EAknSoftkeyExit:
            Exit();
            break;

        case EClientInstallerCommand1:
            {
            // Load a string from the resource file and display it
            HBufC* textResource = StringLoader::LoadLC( R_HEWB_COMMAND1_TEXT );
            CAknInformationNote* informationNote;

            informationNote = new ( ELeave ) CAknInformationNote;

            // Show the information Note with
            // textResource loaded with StringLoader.
            informationNote->ExecuteLD( *textResource);

            // Pop HBuf from CleanUpStack and Destroy it.
            CleanupStack::PopAndDestroy( textResource );
            }
            break;

        default:
            Panic( EClientInstallerUi );
            break;
        }
    }

// End of File