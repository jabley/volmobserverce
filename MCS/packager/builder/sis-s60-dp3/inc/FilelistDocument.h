/*
* ==============================================================================
*  Name        : FilelistDocument.h
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

#ifndef FILELISTDOCUMENT_H
#define FILELISTDOCUMENT_H

// INCLUDES
#include <akndoc.h>
   
// CONSTANTS

// FORWARD DECLARATIONS
class  CEikAppUi;

// CLASS DECLARATION

/**
*  CFilelistDocument application class.
*/
class CFilelistDocument : public CAknDocument
    {
    public: // Constructors and destructor
        /**
        * Two-phased constructor.
        */
        static CFilelistDocument* NewL( CEikApplication& aApp );

        /**
        * Destructor.
        */
        virtual ~CFilelistDocument();

    public: // New functions

    public: // Functions from base classes
    protected:  // New functions

    protected:  // Functions from base classes

    private:

        /**
        * Symbian default constructor.
        */
        CFilelistDocument( CEikApplication& aApp );
        void ConstructL();

    private:

        /**
        * From CEikDocument, create CFilelistAppUi "App UI" object.
        */
        CEikAppUi* CreateAppUiL();
    };

#endif

// End of File

