/*
* ==============================================================================
*  Name        : FilelistAppui.h
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

#ifndef FILELISTAPPUI_H
#define FILELISTAPPUI_H

// INCLUDES
#include <aknappui.h>

// FORWARD DECLARATIONS
class CFilelistContainer;


// CLASS DECLARATION

/**
* Application UI class.
* Provides support for the following features:
* - EIKON control architecture
* 
*/
class CFilelistAppUi : public CAknAppUi
    {
    public: // // Constructors and destructor

        /**
        * Symbian default constructor.
        */      
        void ConstructL();

        /**
        * Destructor.
        */      
        ~CFilelistAppUi();
        
    public: // New functions


	// start Nokia OSS browser
	void StartBrowser();


    private:
        // From MEikMenuObserver
        void DynInitMenuPaneL(TInt aResourceId,CEikMenuPane* aMenuPane);

    private:
        /**
        * From CEikAppUi, takes care of command handling.
        * @param aCommand command to be handled
        */
        void HandleCommandL(TInt aCommand);

        /**
        * From CEikAppUi, handles key events.
        * @param aKeyEvent Event to handled.
        * @param aType Type of the key event. 
        * @return Response code (EKeyWasConsumed, EKeyWasNotConsumed). 
        */
        virtual TKeyResponse HandleKeyEventL(
            const TKeyEvent& aKeyEvent,TEventCode aType);
        /**
        * From CEikAppUi, handles key events.
        * @param aType The type of resources that have changed
        */    
        virtual void HandleResourceChangeL( TInt aType );            

    private: //Data
        CFilelistContainer* iAppContainer; 
    };

#endif

// End of File
