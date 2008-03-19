/*
* ==============================================================================
*  Name        : FilelistDocument.cpp
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
#include "FilelistDocument.h"
#include "FilelistAppui.h"

// ================= MEMBER FUNCTIONS ==========================================

// -----------------------------------------------------------------------------
// CFilelistDocument::CFilelistDocument()
// constructor
// -----------------------------------------------------------------------------
//
CFilelistDocument::CFilelistDocument( CEikApplication& aApp )
    : CAknDocument( aApp )    
    {
    // no implementation required 
    }

// -----------------------------------------------------------------------------
// CFilelistDocument::~CFilelistContainer()
// Destructor
// -----------------------------------------------------------------------------
//
CFilelistDocument::~CFilelistDocument()
    {
    // no implementation required 
    }

// -----------------------------------------------------------------------------
// CFilelistDocument::ConstructL()
// Symbian two phased constructor
// -----------------------------------------------------------------------------
//
void CFilelistDocument::ConstructL()
    {
    // no implementation required 
    }

// -----------------------------------------------------------------------------
// CFilelistDocument::NewL()
// Two-phased constructor.
// -----------------------------------------------------------------------------
//
CFilelistDocument* CFilelistDocument::NewL(
        CEikApplication& aApp )     // CFilelistApp reference
    {
    CFilelistDocument* self = new (ELeave) CFilelistDocument( aApp );
    CleanupStack::PushL( self );
    self->ConstructL();
    CleanupStack::Pop( self );

    return self;
    }
    
// -----------------------------------------------------------------------------
// CFilelistDocument::CreateAppUiL()
// constructs CFilelistAppUi
// -----------------------------------------------------------------------------
//
CEikAppUi* CFilelistDocument::CreateAppUiL()
    {
    return new (ELeave) CFilelistAppUi;
    }

// End of File  
