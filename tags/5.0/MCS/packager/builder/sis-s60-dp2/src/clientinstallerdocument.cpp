
// INCLUDE FILES
#include "ClientInstallerAppUi.h"
#include "ClientInstallerDocument.h"

// ============================ MEMBER FUNCTIONS ===============================

// -----------------------------------------------------------------------------
// CClientInstallerDocument::NewL()
// Two-phased constructor.
// -----------------------------------------------------------------------------
//
CClientInstallerDocument* CClientInstallerDocument::NewL( CEikApplication&
                                                          aApp )
    {
    CClientInstallerDocument* self = NewLC( aApp );
    CleanupStack::Pop( self );
    return self;
    }

// -----------------------------------------------------------------------------
// CClientInstallerDocument::NewLC()
// Two-phased constructor.
// -----------------------------------------------------------------------------
//
CClientInstallerDocument* CClientInstallerDocument::NewLC( CEikApplication&
                                                           aApp )
    {
    CClientInstallerDocument* self =
        new ( ELeave ) CClientInstallerDocument( aApp );

    CleanupStack::PushL( self );
    self->ConstructL();
    return self;
    }

// -----------------------------------------------------------------------------
// CClientInstallerDocument::ConstructL()
// Symbian 2nd phase constructor can leave.
// -----------------------------------------------------------------------------
//
void CClientInstallerDocument::ConstructL()
    {
    // No implementation required
    }

// -----------------------------------------------------------------------------
// CClientInstallerDocument::CClientInstallerDocument()
// C++ default constructor can NOT contain any code, that might leave.
// -----------------------------------------------------------------------------
//
CClientInstallerDocument::CClientInstallerDocument( CEikApplication& aApp )
    : CAknDocument( aApp )
    {
    // No implementation required
    }

// ---------------------------------------------------------------------------
// CClientInstallerDocument::~CClientInstallerDocument()
// Destructor.
// ---------------------------------------------------------------------------
//
CClientInstallerDocument::~CClientInstallerDocument()
    {
    // No implementation required
    }

// ---------------------------------------------------------------------------
// CClientInstallerDocument::CreateAppUiL()
// Constructs CreateAppUi.
// ---------------------------------------------------------------------------
//
CEikAppUi* CClientInstallerDocument::CreateAppUiL()
    {
    // Create the application user interface, and return a pointer to it;
    // the framework takes ownership of this object
    return ( static_cast <CEikAppUi*> ( new ( ELeave )
                                        CClientInstallerAppUi ) );
    }

// End of File