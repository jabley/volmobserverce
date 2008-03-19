/*
* ==============================================================================
*  Name        : FilelistApp.cpp
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
#include    "FilelistApp.h"
#include    "FilelistDocument.h"

// ================= MEMBER FUNCTIONS ==========================================

// -----------------------------------------------------------------------------
// CFilelistApp::AppDllUid()
// Returns application UID
// -----------------------------------------------------------------------------
//
TUid CFilelistApp::AppDllUid() const
    {
    return KUidFilelist;
    }

   
// -----------------------------------------------------------------------------
// CFilelistApp::CreateDocumentL()
// Creates CFilelistDocument object
// -----------------------------------------------------------------------------
//
CApaDocument* CFilelistApp::CreateDocumentL()
    {
    return CFilelistDocument::NewL( *this );
    }

// End of File  

