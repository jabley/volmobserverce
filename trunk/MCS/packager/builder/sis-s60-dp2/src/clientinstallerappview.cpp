

// INCLUDE FILES
#include <coemain.h>
#include "ClientInstallerAppView.h"

// ============================ MEMBER FUNCTIONS ===============================

// -----------------------------------------------------------------------------
// CClientInstallerAppView::NewL()
// Two-phased constructor.
// -----------------------------------------------------------------------------
//
CClientInstallerAppView* CClientInstallerAppView::NewL( const TRect& aRect )
    {
    CClientInstallerAppView* self = CClientInstallerAppView::NewLC( aRect );
    CleanupStack::Pop( self );
    return self;
    }

// -----------------------------------------------------------------------------
// CClientInstallerAppView::NewLC()
// Two-phased constructor.
// -----------------------------------------------------------------------------
//
CClientInstallerAppView* CClientInstallerAppView::NewLC( const TRect& aRect )
    {
    CClientInstallerAppView* self = new ( ELeave ) CClientInstallerAppView;
    CleanupStack::PushL( self );
    self->ConstructL( aRect );
    return self;
    }

// -----------------------------------------------------------------------------
// CClientInstallerAppView::ConstructL()
// Symbian 2nd phase constructor can leave.
// -----------------------------------------------------------------------------
//
void CClientInstallerAppView::ConstructL( const TRect& aRect )
    {
    // Create a window for this application view
    CreateWindowL();

    // Set the windows size
    SetRect( aRect );

    // Activate the window, which makes it ready to be drawn
    ActivateL();
    }

// -----------------------------------------------------------------------------
// CClientInstallerAppView::CClientInstallerAppView()
// C++ default constructor can NOT contain any code, that might leave.
// -----------------------------------------------------------------------------
//
CClientInstallerAppView::CClientInstallerAppView()
    {
    // No implementation required
    }


// -----------------------------------------------------------------------------
// CClientInstallerAppView::~CClientInstallerAppView()
// Destructor.
// -----------------------------------------------------------------------------
//
CClientInstallerAppView::~CClientInstallerAppView()
    {
    // No implementation required
    }


// -----------------------------------------------------------------------------
// CClientInstallerAppView::Draw()
// Draws the display.
// -----------------------------------------------------------------------------
//
void CClientInstallerAppView::Draw( const TRect& /*aRect*/ ) const
    {
    // Get the standard graphics context
    CWindowGc& gc = SystemGc();

    // Gets the control's extent
    TRect rect = Rect();

    // Clears the screen
    gc.Clear( rect );
    }

// End of File