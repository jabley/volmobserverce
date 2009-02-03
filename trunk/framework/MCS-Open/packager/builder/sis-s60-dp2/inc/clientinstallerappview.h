/* Copyright (c) 2004, Nokia. All rights reserved */


#ifndef __CLIENTINSTALLERAPPVIEW_H__
#define __CLIENTINSTALLERAPPVIEW_H__

// INCLUDES
#include <coecntrl.h>

// CLASS DECLARATION
class CClientInstallerAppView : public CCoeControl
    {
    public: // New methods

        /**
        * NewL.
        * Two-phased constructor.
        * Create a CCLIENTINSTALLERAppView object, which will draw itself to aRect.
        * @param aRect The rectangle this view will be drawn to.
        * @return a pointer to the created instance of CCLIENTINSTALLERAppView.
        */
        static CClientInstallerAppView* NewL( const TRect& aRect );

        /**
        * NewLC.
        * Two-phased constructor.
        * Create a CCLIENTINSTALLERAppView object, which will draw itself
        * to aRect.
        * @param aRect Rectangle this view will be drawn to.
        * @return A pointer to the created instance of CCLIENTINSTALLERAppView.
        */
        static CClientInstallerAppView* NewLC( const TRect& aRect );

        /**
        * ~CCLIENTINSTALLERAppView
        * Virtual Destructor.
        */
        virtual ~CClientInstallerAppView();

    public:  // Functions from base classes

        /**
        * From CCoeControl, Draw
        * Draw this CAnimationAppView to the screen.
        * @param aRect the rectangle of this view that needs updating
        */
        void Draw( const TRect& aRect ) const;

    private: // Constructors

        /**
        * ConstructL
        * 2nd phase constructor.
        * Perform the second phase construction of a
        * CCLIENTINSTALLERAppView object.
        * @param aRect The rectangle this view will be drawn to.
        */
        void ConstructL(const TRect& aRect);

        /**
        * CCLIENTINSTALLERAppView.
        * C++ default constructor.
        */
        CClientInstallerAppView();

    };

#endif // __CLIENTINSTALLERAPPVIEW_H__

// End of File