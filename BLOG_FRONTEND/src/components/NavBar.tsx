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
import { Plus, Edit3, LogOut, BookDashed, Sun, Moon } from 'lucide-react';

// 👇 UPDATE THIS PATH to your logo image
import logoImage from '../BLogSpace_Logo.png';

interface NavBarProps {
  isAuthenticated: boolean;
  userProfile?: {
    name: string;
    avatar?: string;
  };
  onLogout: () => void;
  isDark: boolean;
  onToggleTheme: () => void;
}

const NavBar: React.FC<NavBarProps> = ({
  isAuthenticated,
  userProfile,
  onLogout,
  isDark,
  onToggleTheme,
}) => {
  const location = useLocation();
  const [isMenuOpen, setIsMenuOpen] = React.useState(false);

  const menuItems = [
    { name: 'Home', path: '/' },
    { name: 'Categories', path: '/categories' },
    { name: 'Tags', path: '/tags' },
  ];

  const Logo = () => (
    <Link to="/" className="flex items-center gap-2 font-bold text-inherit no-underline">
      <img
        src={logoImage}
        alt="BlogSpace Logo"
        className="h-8 w-auto object-contain rounded-lg"
      />
      <span className="text-lg font-bold">BlogSpace</span>
    </Link>
  );

  // Theme toggle button — reused in both desktop and mobile
  const ThemeToggle = () => (
    <Button
      isIconOnly
      variant="light"
      onPress={onToggleTheme}
      aria-label="Toggle theme"
      className="text-default-600 hover:text-default-900"
    >
      {isDark ? <Sun size={18} /> : <Moon size={18} />}
    </Button>
  );

  const AuthItems = () => (
    <>
      {/* Theme toggle */}
      <ThemeToggle />

      {isAuthenticated ? (
        <>
          <Button
            as={Link}
            to="/posts/drafts"
            color="secondary"
            variant="flat"
            startContent={<BookDashed size={16} />}
          >
            Draft Posts
          </Button>
          <Button
            as={Link}
            to="/posts/new"
            color="primary"
            variant="flat"
            startContent={<Plus size={16} />}
          >
            New Post
          </Button>
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
        </>
      ) : (
        <Button as={Link} to="/login" variant="flat">
          Log In
        </Button>
      )}
    </>
  );

  return (
    <Navbar
      isBordered
      isMenuOpen={isMenuOpen}
      onMenuOpenChange={setIsMenuOpen}
      maxWidth="full"
      className="mb-6"
      style={{ overflow: 'visible' }}
    >
      {/* ── Mobile: hamburger left ── */}
      <NavbarContent className="sm:hidden" justify="start">
        <NavbarMenuToggle />
      </NavbarContent>

      {/* ── Mobile: logo center ── */}
      <NavbarContent className="sm:hidden" justify="center">
        <NavbarBrand>
          <Logo />
        </NavbarBrand>
      </NavbarContent>

      {/* ── Mobile: theme toggle + auth right ── */}
      <NavbarContent className="sm:hidden" justify="end">
        <ThemeToggle />
        {isAuthenticated ? (
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
              <DropdownItem key="new" startContent={<Plus size={16} />}>
                <Link to="/posts/new">New Post</Link>
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
        ) : (
          <Button as={Link} to="/login" variant="flat" size="sm">
            Log In
          </Button>
        )}
      </NavbarContent>

      {/* ══════════════════════════════════════════════════
          DESKTOP LAYOUT — CSS grid for perfect 3-column layout
          Left  → Logo
          Center → Nav links (always truly centered)
          Right  → Theme toggle + Auth items
      ══════════════════════════════════════════════════ */}
      <NavbarContent className="hidden sm:grid w-full" style={{ display: 'none' }} />

      <div
        className="hidden sm:grid w-full items-center"
        style={{
          gridTemplateColumns: '1fr auto 1fr',
          height: '100%',
        }}
      >
        {/* LEFT — Logo */}
        <div className="flex items-center justify-start">
          <Logo />
        </div>

        {/* CENTER — Nav links */}
        <div className="flex items-center gap-10">
          {menuItems.map((item) => (
            <Link
              key={item.path}
              to={item.path}
              className={`text-sm font-medium transition-colors whitespace-nowrap ${
                location.pathname === item.path
                  ? 'text-primary'
                  : 'text-default-600 hover:text-default-900'
              }`}
            >
              {item.name}
            </Link>
          ))}
        </div>

        {/* RIGHT — Theme toggle + Auth items */}
        <div className="flex items-center justify-end gap-3">
          <AuthItems />
        </div>
      </div>

      {/* ── Mobile dropdown menu ── */}
      <NavbarMenu>
        {menuItems.map((item) => (
          <NavbarMenuItem key={item.path}>
            <Link
              to={item.path}
              className={`w-full text-base ${
                location.pathname === item.path
                  ? 'text-primary font-semibold'
                  : 'text-default-600'
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