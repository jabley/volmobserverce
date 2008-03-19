


// INCLUDE FILES
#include "ClientInstallerDocument.h"
#include "ClientInstallerApplication.h"
#include <EikDll.h>
#include <apgcli.h> // apgrfx.lib
#include <apgtask.h>
#include <EIKENV.h>
#include <f32file.h>

// ============================ MEMBER FUNCTIONS ===============================

// UID for the application;
// this should correspond to the uid defined in the mmp file
const TUid KUidClientInstallerApp = { ${application.UID} };

// -----------------------------------------------------------------------------
// CClientInstallerApplication::CreateDocumentL()
// Creates CApaDocument object
// -----------------------------------------------------------------------------
//
CApaDocument* CClientInstallerApplication::CreateDocumentL()
    {

    TBuf<256> str;

    _LIT(KFilePrefix,"FILE:");

  	_LIT(KCDrive,"C:");

  	_LIT(Kcheck, "\\system\\apps\\ClientInstaller\\welcome.html");

    // Construct the check for the C drive

    str.Copy(KCDrive);
    str.Append(Kcheck);
    if ( FileExists(str) ) {

		// The specified file exists, so start the browser to view it.
		str.Copy(KFilePrefix);
		str.Append(KCDrive);
		str.Append(Kcheck);
  		StartBrowser(str);
    }


    // Create an ClientInstaller document, and return a pointer to it
    return (static_cast<CApaDocument*>
                    ( CClientInstallerDocument::NewL( *this ) ) );
    }

// -----------------------------------------------------------------------------
// CClientInstallerApplication::AppDllUid()
// Returns application UID
// -----------------------------------------------------------------------------
//
TUid CClientInstallerApplication::AppDllUid() const
    {
    // Return the UID for the ClientInstaller application
    return KUidClientInstallerApp;
    }


// -----------------------------------------------------------------------------
// CClientInstallerApplication::FileExists(filename)
// Check to see if a specified file exists
// -----------------------------------------------------------------------------
//

bool CClientInstallerApplication::FileExists(const TDesC& checkfile)
    {

 	   	RFs fsSession;
	   	fsSession.Connect();
	   	CleanupClosePushL(fsSession);

	   	RFile f1;

	    TInt err=f1.Open(fsSession, checkfile, EFileRead);
  	    if (err == KErrNone) {
 	       return true;
		} else {
		   return false;
		}
		f1.Close();
	}

// -----------------------------------------------------------------------------
// CClientInstallerApplication::StartBrowser(URL)
// Start up a NETFRONT browser on a given URL
// -----------------------------------------------------------------------------
//
void CClientInstallerApplication::StartBrowser(const TDesC& aUrl)
	{
	HBufC* param = HBufC::NewLC( 256 );
	param->Des().Format( _L( "4 %S" ),&aUrl );

	const TInt KWmlBrowserUid = ${browser.UID};  // NETFRONT Browser 3.3

	TUid id( TUid::Uid( KWmlBrowserUid ) );

	TApaTaskList taskList( CEikonEnv::Static()->WsSession() );
	TApaTask task = taskList.FindApp( id );
	if ( task.Exists() )
	{
	     HBufC8* param8 = HBufC8::NewLC( param->Length() );
	     param8->Des().Append( *param );
	     task.SendMessage( TUid::Uid( 0 ), *param8 ); // Uid is not used
	     CleanupStack::PopAndDestroy(); // param8
	}
	else
	{
	     RApaLsSession appArcSession;
	     User::LeaveIfError(appArcSession.Connect()); // connect to AppArc server
	     TThreadId id;
	     appArcSession.StartDocument( *param, TUid::Uid( KWmlBrowserUid ), id );
	     appArcSession.Close();
	}
	CleanupStack::PopAndDestroy(); // param
	}






// End of File