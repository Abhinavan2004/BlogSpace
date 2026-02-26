import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  NavbarMenu,
  NavbarMenuItem,
  NavbarMenuToggle,
  Button,
  Avatar,
  Dropdown,
  DropdownTrigger,
  DropdownMenu,
  DropdownItem,
} from '@nextui-org/react';
import { Plus, BookOpen, Edit3, LogOut, BookDashed } from 'lucide-react';

// 👇 UPDATE THIS PATH to your logo image
import logoImage from '../BLogSpace_Logo.png';

interface NavBarProps {
  isAuthenticated: boolean;
  userProfile?: {
    name: string;
    avatar?: string;
  };
  onLogout: () => void;
}

const NavBar: React.FC<NavBarProps> = ({
  isAuthenticated,
  userProfile,
  onLogout,
}) => {
  const location = useLocation();
  const [isMenuOpen, setIsMenuOpen] = React.useState(false);

  const menuItems = [
    { name: 'Home', path: '/' },
    { name: 'Categories', path: '/categories' },
    { name: 'Tags', path: '/tags' },
  ];

  const Logo = () => (
    <Link to="/" className="flex items-center gap-2 font-bold text-inherit no-underline ">
      <img
        src={logoImage}
        alt="BlogSpace Logo"
        className="h-8 w-auto object-contain rounded-lg"
      />
      <span className="text-lg font-bold">BlogSpace</span>
    </Link>
  );

  return (
    <Navbar
      isBordered
      isMenuOpen={isMenuOpen}
      onMenuOpenChange={setIsMenuOpen}
      className="mb-6"
    >
      {/* Mobile: hamburger left */}
      <NavbarContent className="sm:hidden" justify="start">
        <NavbarMenuToggle />
      </NavbarContent>

      {/* Mobile: logo center */}
      <NavbarContent className="sm:hidden pr-3" justify="center">
        <NavbarBrand>
          <Logo />
        </NavbarBrand>
      </NavbarContent>

      {/* Desktop: logo + links left */}
      <NavbarContent className="hidden sm:flex gap-4" justify="start">
        <NavbarBrand>
          <Logo />
        </NavbarBrand>
        {menuItems.map((item) => (
          <NavbarItem key={item.path} isActive={location.pathname === item.path}>
            <Link
              to={item.path}
              className={`text-sm ${
                location.pathname === item.path ? 'text-primary' : 'text-default-600'
              }`}
            >
              {item.name}
            </Link>
          </NavbarItem>
        ))}
      </NavbarContent>

      {/* Right side */}
      <NavbarContent justify="end">
        {isAuthenticated ? (
          <>
            <NavbarItem>
              <Button
                as={Link}
                to="/posts/drafts"
                color="secondary"
                variant="flat"
                startContent={<BookDashed size={16} />}
              >
                Draft Posts
              </Button>
            </NavbarItem>
            <NavbarItem>
              <Button
                as={Link}
                to="/posts/new"
                color="primary"
                variant="flat"
                startContent={<Plus size={16} />}
              >
                New Post
              </Button>
            </NavbarItem>
            <NavbarItem>
              <Dropdown placement="bottom-end">
                <DropdownTrigger>
                  <Avatar
                    isBordered
                    as="button"
                    className="transition-transform"
                    src={userProfile?.avatar}
                    name={userProfile?.name}
                  />
                </DropdownTrigger>
                <DropdownMenu aria-label="User menu">
                  <DropdownItem key="drafts" startContent={<Edit3 size={16} />}>
                    <Link to="/posts/drafts">My Drafts</Link>
                  </DropdownItem>
                  <DropdownItem
                    key="logout"
                    startContent={<LogOut size={16} />}
                    className="text-danger"
                    color="danger"
                    onPress={onLogout}
                  >
                    Log Out
                  </DropdownItem>
                </DropdownMenu>
              </Dropdown>
            </NavbarItem>
          </>
        ) : (
          <NavbarItem>
            <Button as={Link} to="/login" variant="flat">
              Log In
            </Button>
          </NavbarItem>
        )}
      </NavbarContent>

      {/* Mobile dropdown menu */}
      <NavbarMenu>
        {menuItems.map((item) => (
          <NavbarMenuItem key={item.path}>
            <Link
              to={item.path}
              className={`w-full ${
                location.pathname === item.path ? 'text-primary' : 'text-default-600'
              }`}
              onClick={() => setIsMenuOpen(false)}
            >
              {item.name}
            </Link>
          </NavbarMenuItem>
        ))}
      </NavbarMenu>
    </Navbar>
  );
};

export default NavBar;
