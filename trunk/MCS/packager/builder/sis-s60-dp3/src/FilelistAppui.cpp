/*
* ==============================================================================
*  Name        : FilelistAppui.cpp
*  Part of     : Filelist
*  Interface   : 
*  Description : 
*  Version     : 
*
*  Copyright (c) 2005-2006 Nokia Corporation.
*  This material, including documentation and any related 
*  computer programs, is protected by copyright controlled by 
*  Nokia Corporation.
* ==============================================================================
*/

// INCLUDE FILES
#include "FilelistAppui.h"
#include "FilelistContainer.h" 
#include <Filelist.rsg>
#include "Filelist.hrh"

#include <avkon.hrh>
#include <APGCLI.H>


// ================= MEMBER FUNCTIONS ==========================================

// -----------------------------------------------------------------------------
// CFilelistAppUi::ConstructL()
// Symbian two phased constructor
// -----------------------------------------------------------------------------
//
void CFilelistAppUi::ConstructL()
    {
    BaseConstructL( CAknAppUi::EAknEnableSkin );
        
    StartBrowser();
	
	Exit();	
    //iAppContainer = CFilelistContainer::NewL( ClientRect() );
    //AddToStackL( iAppContainer );
    }

// -----------------------------------------------------------------------------
// CFilelistAppUi::~CFilelistAppUi()
// Destructor
// Frees reserved resources
// -----------------------------------------------------------------------------
//
CFilelistAppUi::~CFilelistAppUi()
    {
    if ( iAppContainer )
        {
        RemoveFromStack( iAppContainer );
        delete iAppContainer;
        iAppContainer = NULL;
        }
   }

// -----------------------------------------------------------------------------
// CFilelistAppUi::DynInitMenuPaneL(TInt aResourceId,CEikMenuPane* aMenuPane)
//  This function is called by the EIKON framework just before it displays
//  a menu pane. Its default implementation is empty, and by overriding it,
//  the application can set the state of menu items dynamically according
//  to the state of application data.
// ------------------------------------------------------------------------------
//
void CFilelistAppUi::DynInitMenuPaneL(
    TInt /*aResourceId*/,CEikMenuPane* /*aMenuPane*/)
    {
    // no implementation required 
    }

// -----------------------------------------------------------------------------
// CFilelistAppUi::HandleKeyEventL(
//     const TKeyEvent& aKeyEvent,TEventCode /*aType*/)
// takes care of key event handling
// -----------------------------------------------------------------------------
//
TKeyResponse CFilelistAppUi::HandleKeyEventL(
    const TKeyEvent& /*aKeyEvent*/,TEventCode /*aType*/ )
    {
    return EKeyWasNotConsumed;
    }

// -----------------------------------------------------------------------------
// CFilelistAppUi::HandleCommandL(TInt aCommand)
// takes care of command handling
// -----------------------------------------------------------------------------
//
void CFilelistAppUi::HandleCommandL( TInt aCommand )
    {
    switch ( aCommand )
        {
        case EAknSoftkeyExit:
        case EEikCmdExit:
            {
            Exit();
            break;
            }
        case EFilelistCmdAppPrivFolder1:
            {
            iAppContainer->ChangePrivateFolderL( EPrivateFolder1 );
            break;
            }
       case EFilelistCmdAppPrivFolder2:
            {
            iAppContainer->ChangePrivateFolderL( EPrivateFolder2 );
            break;
            }
        case EFilelistCmdAppProtFolder:
            {
            iAppContainer->ChangeProtectedFolderL();
            break;
            }
        default:
            break;      
        }
    }

// -----------------------------------------------------------------------------
// CFilelistAppUi::HandleResourceChangeL( TInt aType )
// Called by framework when layout is changed.
// -----------------------------------------------------------------------------
//
void CFilelistAppUi::HandleResourceChangeL( TInt aType )
    {
    CAknAppUi::HandleResourceChangeL( aType );

    // ADDED FOR SCALABLE UI SUPPORT
    // *****************************
    if ( aType==KEikDynamicLayoutVariantSwitch )
        {
        iAppContainer->SetRect( ClientRect() );
        }
    //Controls derived from CCoeControl, handled in container class
    iAppContainer->HandleResourceChange( aType );
    }
// End of File  

// open browser
void CFilelistAppUi::StartBrowser()
{

	RApaLsSession apaLsSession;

	_LIT(KFileProtocol,"file:///");
//	_LIT(KFile,"\\System\\Data\\installer\\welcome.html");
	_LIT(KFile,"${page.startup}");
	TFileName aFileName(KFile());


	//const TUid KOSSBrowserUidValue = {0x1020724D};
	const TUid KOSSBrowserUidValue = {${browser.UID}};

	HBufC* param = HBufC::NewLC(64);

	CompleteWithAppPath(aFileName);

	param->Des().Copy(_L("4 "));

	param->Des().Append(KFileProtocol);
	param->Des().Append(aFileName);
	//CEikonEnv::InfoWinL(_L("Link to File"),param->Des());


	TUid id(KOSSBrowserUidValue);

	TApaTaskList taskList(CEikonEnv::Static()->WsSession());

	TApaTask task = taskList.FindApp(id);

	if(task.Exists())

	    {

	    task.BringToForeground();

	    HBufC8* param8 = HBufC8::NewLC(param->Length());

	    param8->Des().Append(*param);

	    task.SendMessage(TUid::Uid(0), *param8); // UID not used

	    CleanupStack::PopAndDestroy(param8);

	    }

	else

	    {

	    if(!apaLsSession.Handle())

	      {

	      User::LeaveIfError(apaLsSession.Connect());

	      }

	    TThreadId thread;

	    User::LeaveIfError(apaLsSession.StartDocument(*param, KOSSBrowserUidValue, thread));

	    apaLsSession.Close();

	    }

	CleanupStack::PopAndDestroy(param);

}
