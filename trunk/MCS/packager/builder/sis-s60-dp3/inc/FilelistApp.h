/*
* ==============================================================================
*  Name        : FilelistApp.h
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

#ifndef FILELISTAPP_H
#define FILELISTAPP_H

// INCLUDES
#include <aknapp.h>

// CONSTANTS
// UID of the application
const TUid KUidFilelist = { ${application.UID} };

// CLASS DECLARATION

/**
* CFilelistApp application class.
* Provides factory to create concrete document object.
* 
*/
class CFilelistApp : public CAknApplication
    {
    
    public: // Functions from base classes
    private:

        /**
        * From CApaApplication, creates CFilelistDocument document object.
        * @return A pointer to the created document object.
        */
        CApaDocument* CreateDocumentL();
        
        /**
        * From CApaApplication, returns application's UID (KUidFilelist).
        * @return The value of KUidFilelist.
        */
        TUid AppDllUid() const;
    };

#endif

// End of File

