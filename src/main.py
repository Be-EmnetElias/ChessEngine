


import pygame
import sys
import numpy as np

from board import Board

class Main:

    def __init__(self):

        pygame.init()
        self.screen = pygame.display.set_mode((800,800))
        pygame.display.set_caption("Chess")

        self.Board = Board("default")

    def mainloop(self):

        # Create background
        bg = pygame.image.load("C:\\Users\\Be-Emnet Elias\\OneDrive - Kent School District\\Desktop\\Chess\\imgs\\bg_green.png")
        bg = pygame.transform.scale(bg,(800,800))
        
        # Slice pieces.png into separate pieces
        pieces = pygame.image.load("C:\\Users\\Be-Emnet Elias\\OneDrive - Kent School District\\Desktop\\Chess\\imgs\\pieces.png")
        piece_width = pieces.get_width() // 6
        piece_height = pieces.get_height() // 2

        imgs = []

        for i in range(12):
            row = i // 6
            col = i % 6

            piece = pieces.subsurface(pygame.Rect(col * piece_width, row * piece_height, piece_width, piece_height))
            piece = pygame.transform.scale(piece,(100,100))

            imgs.append(piece)
        
        board = self.Board.board

        # The piece that is being interacted with. Note that
        # this main.py will ONLY interact with a pieces pixel location
        clicked_piece = None

        while True:
            self.screen.blit(bg,(0,0))
            for event in pygame.event.get():

                # click
                if event.type == pygame.MOUSEBUTTONDOWN:

                    clicked_row = event.pos[1] // 100
                    clicked_col = event.pos[0] // 100

                    clicked_piece = board[clicked_row][clicked_col]
                    #print(f"{clicked_piece} was clicked at {clicked_col},{clicked_row}")
                
                # mouse motion
                elif clicked_piece and event.type == pygame.MOUSEMOTION:
                    
                    clicked_piece.x = event.pos[0] - 40
                    clicked_piece.y = event.pos[1] - 50

                # release click
                elif clicked_piece and event.type == pygame.MOUSEBUTTONUP:
                    x,y = event.pos
                    col, row = x//100, y//100

                    # user_valid_move calls move_piece which also updates pixel location
                    if not self.Board.user_valid_move(clicked_piece,(col,row)):
                        clicked_piece.x = clicked_piece.col * 100
                        clicked_piece.y = clicked_piece.row * 100
            
                    clicked_piece = None
                    # self.Board.printBoard()

                # quit
                elif event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()

            for row in range(8):
                for col in range(8):
                    if board[row][col]:
                        piece = board[row][col]
                        index = piece.img_index
                        if piece != clicked_piece:
                            self.screen.blit(imgs[index],(piece.x,piece.y))
            if clicked_piece:
                self.screen.blit(imgs[clicked_piece.img_index],(clicked_piece.x,clicked_piece.y))
            pygame.display.update()

main = Main()
main.mainloop()